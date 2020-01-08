package com.metazion.essence.share.helper;

import com.alibaba.fastjson.JSON;

import java.util.Optional;

public class JsonHelper {

    public static <T> String toJsonString(T obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> Optional<T> fromJsonString(String string, Class<T> clazz) {
        try {
            return Optional.ofNullable(JSON.parseObject(string, clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
