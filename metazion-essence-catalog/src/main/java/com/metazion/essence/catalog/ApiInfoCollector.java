package com.metazion.essence.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiInfoCollector {

    private ApplicationContext applicationContext;

    private ApiConfigProperties apiConfigProperties;

    private Map<String, ApiClassInfo> classInfos = new HashMap<>();

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

    public void collect() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Controller.class);
        for (Object value : beansWithAnnotation.values()) {
            if (filterClassInfo(value.getClass())) {
                collectClassInfo(value.getClass());
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
            classInfo.setPaths(requestMapping.path());
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
        String type = annotation.method().length > 0 ? annotation.method()[0].toString() : "ANY";
        methodInfo.setType(type);
        methodInfo.setPaths(annotation.path());
    }

    private void collectGetMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setType("GET");

        GetMapping annotation = AnnotationUtils.findAnnotation(method, GetMapping.class);
        methodInfo.setPaths(annotation.path());
    }

    private void collectPutMethodInfo(Method method, ApiMethodInfo methodInfo) {
        methodInfo.setType("PUT");

        PutMapping annotation = AnnotationUtils.findAnnotation(method, PutMapping.class);
        methodInfo.setPaths(annotation.path());
    }
}
