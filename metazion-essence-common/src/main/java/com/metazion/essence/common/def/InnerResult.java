package com.metazion.essence.common.def;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InnerResult<T, E> {

    private int code;

    @JsonIgnore
    private T data;

    @JsonIgnore
    private E extra;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public E getExtra() {
        return extra;
    }

    public void setExtra(E extra) {
        this.extra = extra;
    }
}
