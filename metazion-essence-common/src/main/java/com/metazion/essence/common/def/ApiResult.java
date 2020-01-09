package com.metazion.essence.common.def;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResult<T, E> {

    private int code;
    private String msg;
    private T data;

    @JsonIgnore
    private E extra;

    public static <T, E> ApiResult<T, E> full(int code, T data, E extra) {
        ApiResult<T, E> ar = new ApiResult<>();
        ar.setCode(code);
        ar.setData(data);
        ar.setExtra(extra);
        return ar;
    }

    public static <E> ApiResult<Object, E> pure(int code, E extra) {
        return full(code, null, extra);
    }

    public static <T, E> ApiResult<T, E> success(T data, E extra) {
        return full(ResultType.SUCCESS.getCode(), data, extra);
    }

    public static <T, E> ApiResult<T, E> failed(T data, E extra) {
        return full(ResultType.FAILED.getCode(), data, extra);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
