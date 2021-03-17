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
import java.util.regex.Pattern;

@Component
public class ApiInfoCollector {

    private final Map<String, ApiClassInfo> classInfos = new TreeMap<>();
    private final Set<String> apis = new TreeSet<>();
    private final Set<String> restfulApis = new TreeSet<>();
    private final Map<String, ApiMethodInfo> apiInfos = new TreeMap<>();
    private final Map<String, ApiMethodInfo> restfulApiInfos = new TreeMap<>();
    private final Map<String, TypeClassInfo> methodTypeInfoCache = new TreeMap<>();
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

    public Set<String> getRestfulApis() {
        return restfulApis;
    }

    public Map<String, ApiMethodInfo> getApiInfos() {
        return apiInfos;
    }

    public Map<String, ApiMethodInfo> getRestfulApiInfos() {
        return restfulApiInfos;
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
                        String path = String.format("/%s/%s", superiorPath, methodInfo.getPaths()[i])
                                          .replaceAll("(?<=/)/", "")
                                          .replaceAll("(?<=.)/$", "");
                        methodInfo.getPaths()[i] = path;

                        for (String type : methodInfo.getTypes()) {
                            String api = String.format("%s %s", path, type);
                            apis.add(api);
                            apiInfos.put(api, methodInfo);
                            if (methodInfo.isRestful()) {
                                restfulApis.add(api);
                                restfulApiInfos.put(api, methodInfo);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean filterClassInfo(Class<?> clazz) {
        String[] excludePatterns = apiConfigurationProperties.getControllerExcludePatterns();
        for (String pattern : excludePatterns) {
            if (checkPatternMatch(pattern, clazz.getCanonicalName())) {
                return false;
            }
        }

        String[] includePatterns = apiConfigurationProperties.getControllerIncludePatterns();
        for (String pattern : includePatterns) {
            if (checkPatternMatch(pattern, clazz.getCanonicalName())) {
                return true;
            }
        }

        return includePatterns.length == 0;
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

            if (method.isAnnotationPresent(PutMapping.class)) {
                collectPutMethodInfo(method, methodInfo);
                classInfo.addMethodInfo(methodInfo);
            }

            if (method.isAnnotationPresent(PatchMapping.class)) {
                collectPatchMethodInfo(method, methodInfo);
                classInfo.addMethodInfo(methodInfo);
            }

            if (method.isAnnotationPresent(DeleteMapping.class)) {
                collectDeleteMethodInfo(method, methodInfo);
                classInfo.addMethodInfo(methodInfo);
            }
        }

        classInfos.put(classInfo.getSimpleName(), classInfo);
    }

    private void collectMethodCommonInfo(Method method, ApiMethodInfo methodInfo) {
        methodTypeInfoCache.clear();

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

    private void collectPutMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setTypes("PUT");

        PutMapping annotation = AnnotationUtils.findAnnotation(method, PutMapping.class);
        assert annotation != null;
        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private void collectPatchMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setTypes("PATCH");

        PatchMapping annotation = AnnotationUtils.findAnnotation(method, PatchMapping.class);
        assert annotation != null;
        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private void collectDeleteMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setTypes("DELETE");

        DeleteMapping annotation = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        assert annotation != null;
        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private TypeClassInfo getTypeClassInfo(Class<?> clazz, Type type) {
        String typeName = clazz.getCanonicalName();
        if (type instanceof ParameterizedType) {
            typeName = type.getTypeName()
                           .replaceAll("<", "&lt;")
                           .replaceAll(">", "&gt;");
        }

        TypeClassInfo classInfo = new TypeClassInfo();
        classInfo.setCanonicalName(typeName);

        if (methodTypeInfoCache.containsKey(typeName)) {
            return classInfo;
        }

        methodTypeInfoCache.put(typeName, classInfo);

        if (!clazz.isPrimitive() && !"java.lang.String".equals(clazz.getCanonicalName())) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

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

    private boolean checkPatternMatch(String regex, String input) {
        try {
            return Pattern.matches(regex, input);
        } catch (Exception e) {
            return false;
        }
    }
}
