package com.metazion.essence.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiClassInfo {

    private String simpleName = "";
    private String canonicalName = "";
    private String[] paths = {};
    private boolean restful = false;
    private List<ApiMethodInfo> methodInfos = new ArrayList<>();

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String... paths) {
        this.paths = paths.clone();
    }

    public boolean isRestful() {
        return restful;
    }

    public void setRestful(boolean restful) {
        this.restful = restful;
    }

    public List<ApiMethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public void setMethodInfos(List<ApiMethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }

    public void addMethodInfo(ApiMethodInfo methodInfo) {
        this.methodInfos.add(methodInfo);
    }
}
