package com.metazion.essence.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TypeFieldInfo {

    private String name;
    private TypeClassInfo type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeClassInfo getType() {
        return type;
    }

    public void setType(TypeClassInfo type) {
        this.type = type;
    }
}
