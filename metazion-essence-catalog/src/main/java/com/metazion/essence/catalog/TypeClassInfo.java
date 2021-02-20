package com.metazion.essence.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TypeClassInfo {

    private String canonicalName;
    private List<TypeFieldInfo> fieldInfos = new ArrayList<>();

    public String getCanonicalName() {
        return canonicalName;
    }

    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
    }

    public List<TypeFieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<TypeFieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public void addFieldInfos(TypeFieldInfo fieldInfo) {
        this.fieldInfos.add(fieldInfo);
    }
}
