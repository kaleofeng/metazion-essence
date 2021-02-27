package com.metazion.essence.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiMethodInfo {

    private String name;
    private String[] paths;
    private String[] types;
    private boolean restful;
    private TypeClassInfo returnType;
    private List<ApiParameterInfo> parameterInfos = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPaths() {
        return paths.clone();
    }

    public void setPaths(String... paths) {
        this.paths = paths.clone();
    }

    public String[] getTypes() {
        return types.clone();
    }

    public void setTypes(String... types) {
        this.types = types.clone();
    }

    public boolean isRestful() {
        return restful;
    }

    public void setRestful(boolean restful) {
        this.restful = restful;
    }

    public TypeClassInfo getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeClassInfo returnType) {
        this.returnType = returnType;
    }

    public List<ApiParameterInfo> getParameterInfos() {
        return parameterInfos;
    }

    public void setParameterInfos(List<ApiParameterInfo> parameterInfos) {
        this.parameterInfos = parameterInfos;
    }

    public void addParameterInfo(ApiParameterInfo parameterInfo) {
        this.parameterInfos.add(parameterInfo);
    }
}
