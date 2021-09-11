package com.metazion.essence.catalog.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.metazion.essence.catalog.ApiInfoCollector;
import com.metazion.essence.catalog.ApiMethodInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/_apis_")
public class ApiInfoController {

    private ApiInfoCollector apiInfoCollector;

    @Autowired
    public void setApiInfoCollector(ApiInfoCollector apiInfoCollector) {
        this.apiInfoCollector = apiInfoCollector;
    }

    @GetMapping("")
    public String list(@RequestParam(value = "restful", required = false, defaultValue = "0") int restful) {
        if (restful == 0) {
            return JSON.toJSONString(apiInfoCollector.getApis(), SerializerFeature.DisableCircularReferenceDetect);
        } else {
            return JSON.toJSONString(apiInfoCollector.getRestfulApis(), SerializerFeature.DisableCircularReferenceDetect);
        }
    }

    @GetMapping("/details")
    public String details(@RequestParam(value = "restful", required = false, defaultValue = "0") int restful) {
        if (restful == 0) {
            return JSON.toJSONString(apiInfoCollector.getApiInfos(), SerializerFeature.DisableCircularReferenceDetect);
        } else {
            return JSON.toJSONString(apiInfoCollector.getRestfulApiInfos(), SerializerFeature.DisableCircularReferenceDetect);
        }
    }

    @GetMapping("/detail")
    public String detail(@RequestParam("api") String api) {
        ApiMethodInfo methodInfo = apiInfoCollector.getApiInfo(api);
        return Optional.ofNullable(methodInfo)
            .map(t -> JSON.toJSONString(t, SerializerFeature.DisableCircularReferenceDetect))
            .orElse("");
    }

    @GetMapping("/controllers")
    public String controllers() {
        return JSON.toJSONString(apiInfoCollector.getClassInfos(), SerializerFeature.DisableCircularReferenceDetect);
    }
}
