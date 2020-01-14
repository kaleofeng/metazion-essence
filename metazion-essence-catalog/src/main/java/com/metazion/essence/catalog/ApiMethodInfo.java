package com.metazion.essence.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiMethodInfo {

    private String name = "";
    private String[] paths = {};
    private String type = "";
    private boolean restful = false;
    private String returnType = "";
    private List<ApiParameterInfo> parameterInfos = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String... paths) {
        this.paths = paths.clone();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRestful() {
        return restful;
    }

    public void setRestful(boolean restful) {
        this.restful = restful;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
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

    @Override
    public String toString() {
        return String.format("%s %s", type, name);
    }
}
