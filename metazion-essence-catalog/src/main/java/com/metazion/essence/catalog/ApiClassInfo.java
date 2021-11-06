package com.metazion.essence.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiClassInfo {

    private String simpleName;
    private String canonicalName;
    private String[] paths;
    private boolean restful;
    private Set<ApiMethodInfo> methodInfos = new TreeSet<>(Comparator.comparing(ApiMethodInfo::getName));

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
        return paths.clone();
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

    public Set<ApiMethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public void setMethodInfos(Set<ApiMethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }

    public void addMethodInfo(ApiMethodInfo methodInfo) {
        this.methodInfos.add(methodInfo);
    }
}
