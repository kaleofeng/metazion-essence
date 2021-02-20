package com.metazion.essence.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Component
public class ApiInfoCollector {

    private final Map<String, ApiClassInfo> classInfos = new TreeMap<>();
    private final Set<String> apis = new TreeSet<>();
    private final Map<String, ApiMethodInfo> apiInfos = new TreeMap<>();
    private ApplicationContext applicationContext;
    private ApiConfigurationProperties apiConfigurationProperties;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setApiConfigurationProperties(ApiConfigurationProperties apiConfigurationProperties) {
        this.apiConfigurationProperties = apiConfigurationProperties;
    }

    public Map<String, ApiClassInfo> getClassInfos() {
        return classInfos;
    }

    public Set<String> getApis() {
        return apis;
    }

    public Map<String, ApiMethodInfo> getApiInfos() {
        return apiInfos;
    }

    public ApiMethodInfo getApiInfo(String api) {
        return apiInfos.get(api);
    }

    public void perform() {
        collect();
        tidy();
    }

    private void collect() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Controller.class);
        for (Object value : beansWithAnnotation.values()) {
            if (filterClassInfo(value.getClass())) {
                collectClassInfo(value.getClass());
            }
        }
    }

    private void tidy() {
        for (ApiClassInfo classInfo : classInfos.values()) {
            for (ApiMethodInfo methodInfo : classInfo.getMethodInfos()) {
                for (String superiorPath : classInfo.getPaths()) {
                    int nodePathLength = methodInfo.getPaths().length;
                    for (int i = 0; i < nodePathLength; ++i) {
                        String path = superiorPath + methodInfo.getPaths()[i];
                        methodInfo.getPaths()[i] = path;

                        for (String type : methodInfo.getTypes()) {
                            String api = path + " " + type;
                            apis.add(api);
                            apiInfos.put(api, methodInfo);
                        }
                    }
                }
            }
        }
    }

    private boolean filterClassInfo(Class<?> clazz) {
        String[] prefixes = apiConfigurationProperties.getControllerPrefixes();
        for (String prefix : prefixes) {
            if (clazz.getCanonicalName().startsWith(prefix)) {
                return true;
            }
        }
        return prefixes.length == 0;
    }

    private void collectClassInfo(Class<?> clazz) {
        ApiClassInfo classInfo = new ApiClassInfo();
        classInfo.setSimpleName(clazz.getSimpleName());
        classInfo.setCanonicalName(clazz.getCanonicalName());

        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
            assert requestMapping != null;
            classInfo.setPaths(ensureNonEmptyPaths(requestMapping.path()));
        } else {
            classInfo.setPaths("");
        }

        if (clazz.isAnnotationPresent(RestController.class)) {
            classInfo.setRestful(true);
        }

        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            ApiMethodInfo methodInfo = new ApiMethodInfo();
            methodInfo.setRestful(classInfo.isRestful());

            collectMethodCommonInfo(method, methodInfo);

            if (method.isAnnotationPresent(RequestMapping.class)) {
                collectAnyMethodInfo(method, methodInfo);
                classInfo.addMethodInfo(methodInfo);
            }

            if (method.isAnnotationPresent(GetMapping.class)) {
                collectGetMethodInfo(method, methodInfo);
                classInfo.addMethodInfo(methodInfo);
            }

            if (method.isAnnotationPresent(PostMapping.class)) {
                collectPostMethodInfo(method, methodInfo);
                classInfo.addMethodInfo(methodInfo);
            }
        }

        classInfos.put(classInfo.getSimpleName(), classInfo);
    }

    private void collectMethodCommonInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setName(method.getName());
        methodInfo.setReturnType(getTypeClassInfo(method.getReturnType(), method.getGenericReturnType()));

        if (!methodInfo.isRestful() && method.isAnnotationPresent(ResponseBody.class)) {
            methodInfo.setRestful(true);
        }

        int paramCount = method.getParameterCount();
        for (int i = 0; i < paramCount; ++i) {
            Parameter parameter = method.getParameters()[i];
            Type type = method.getGenericParameterTypes()[i];
            ApiParameterInfo parameterInfo = new ApiParameterInfo();
            parameterInfo.setType(getTypeClassInfo(parameter.getType(), type));
            parameterInfo.setName(parameter.getName());
            methodInfo.addParameterInfo(parameterInfo);
        }
    }

    private void collectAnyMethodInfo(Method method, ApiMethodInfo methodInfo) {
        RequestMapping annotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        assert annotation != null;

        int methodLength = annotation.method().length;
        if (methodLength > 0) {
            String[] types = new String[methodLength];
            for (int i = 0; i < methodLength; ++i) {
                types[i] = annotation.method()[i].toString();
            }
            methodInfo.setTypes(types);
        } else {
            methodInfo.setTypes("ANY");
        }

        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private void collectGetMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setTypes("GET");

        GetMapping annotation = AnnotationUtils.findAnnotation(method, GetMapping.class);
        assert annotation != null;
        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private void collectPostMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setTypes("POST");

        PostMapping annotation = AnnotationUtils.findAnnotation(method, PostMapping.class);
        assert annotation != null;
        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private TypeClassInfo getTypeClassInfo(Class<?> clazz, Type type) {
        TypeClassInfo classInfo = new TypeClassInfo();
        classInfo.setCanonicalName(clazz.getCanonicalName());

        if (type instanceof ParameterizedType) {
            String typeName = type.getTypeName()
                                  .replaceAll("<", "&lt;")
                                  .replaceAll(">", "&gt;");
            classInfo.setCanonicalName(typeName);
        }

        if (!clazz.isPrimitive() && !clazz.getCanonicalName().equals("java.lang.String")) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                TypeFieldInfo fieldInfo = new TypeFieldInfo();
                fieldInfo.setName(field.getName());
                fieldInfo.setType(getTypeClassInfo(field.getType(), field.getGenericType()));
                classInfo.addFieldInfos(fieldInfo);
            }
        }

        return classInfo;
    }

    private String[] ensureNonEmptyPaths(String... paths) {
        return paths.length > 0 ? paths : new String[]{""};
    }
}
