package com.metazion.essence.catalog.controller;

import com.alibaba.fastjson.JSON;
import com.metazion.essence.catalog.ApiClassInfo;
import com.metazion.essence.catalog.ApiInfoCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiInfoController {

    private ApiInfoCollector apiInfoCollector;

    @Autowired
    public void setApiInfoCollector(ApiInfoCollector apiInfoCollector) {
        this.apiInfoCollector = apiInfoCollector;
    }

    @GetMapping("/all")
    public String all() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ApiClassInfo classInfo : apiInfoCollector.getClassInfos().values()) {
            stringBuilder.append(JSON.toJSONString(classInfo));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
