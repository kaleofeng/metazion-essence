package com.metazion.essence.catalog.runner;

import com.metazion.essence.catalog.ApiInfoCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApiInfoRunner implements CommandLineRunner {

    private ApiInfoCollector apiInfoCollector;

    @Autowired
    public void setApiInfoCollector(ApiInfoCollector apiInfoCollector) {
        this.apiInfoCollector = apiInfoCollector;
    }

    @Override
    public void run(String... args) {
        apiInfoCollector.perform();
    }
}
