package com.metazion.essence.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ApiInfoCollector {

    private ApplicationContext applicationContext;
    private ApiConfigProperties apiConfigProperties;

    private Map<String, ApiClassInfo> classInfos = new TreeMap<>();

    private List<String> apiList = new ArrayList<>();
    private Map<String, ApiMethodInfo> apiInfos = new TreeMap<>();

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setApiConfigProperties(ApiConfigProperties apiConfigProperties) {
        this.apiConfigProperties = apiConfigProperties;
    }

    public Map<String, ApiClassInfo> getClassInfos() {
        return classInfos;
    }

    public List<String> getApiList() {
        return apiList;
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
                            apiList.add(api);
                            apiInfos.put(api, methodInfo);
                        }
                    }
                }
            }
        }
    }

    private boolean filterClassInfo(Class<?> clazz) {
        String[] prefixes = apiConfigProperties.getControllerPrefixes();
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

            if (method.isAnnotationPresent(PutMapping.class)) {
                collectPutMethodInfo(method, methodInfo);
                classInfo.addMethodInfo(methodInfo);
            }
        }

        classInfos.put(classInfo.getSimpleName(), classInfo);
    }

    private void collectMethodCommonInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setName(method.getName());
        methodInfo.setReturnType(method.getReturnType().getCanonicalName());

        if (!methodInfo.isRestful() && method.isAnnotationPresent(ResponseBody.class)) {
            methodInfo.setRestful(true);
        }

        for (Parameter parameter : method.getParameters()) {
            ApiParameterInfo parameterInfo = new ApiParameterInfo();
            parameterInfo.setType(parameter.getType().getCanonicalName());
            parameterInfo.setName(parameter.getName());
            methodInfo.addParameterInfo(parameterInfo);
        }
    }

    private void collectAnyMethodInfo(Method method, ApiMethodInfo methodInfo) {
        RequestMapping annotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);

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
        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private void collectPutMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setTypes("PUT");

        PutMapping annotation = AnnotationUtils.findAnnotation(method, PutMapping.class);
        methodInfo.setPaths(ensureNonEmptyPaths(annotation.path()));
    }

    private String[] ensureNonEmptyPaths(String... paths) {
        return paths.length > 0 ? paths : new String[]{""};
    }
}
