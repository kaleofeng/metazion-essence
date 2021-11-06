package com.metazion.essence.common.def;

public enum ResultType {

    SUCCESS(10000),
    FAILED(20001),
    TIMEOUT(20002),
    CONSTRAINT_VIOLATION(20003),
    ARGUMENT_INVALID(20004);

    private final int code;

    ResultType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
