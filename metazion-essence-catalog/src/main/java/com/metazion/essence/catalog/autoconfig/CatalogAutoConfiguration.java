package com.metazion.essence.catalog.autoconfig;

import com.metazion.essence.catalog.ApiConfigurationProperties;
import com.metazion.essence.catalog.ApiInfoCollector;
import com.metazion.essence.catalog.controller.ApiInfoController;
import com.metazion.essence.catalog.runner.ApiInfoRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiConfigurationProperties.class)
@ConditionalOnProperty(name = "metazion.essence.catalog.enabled", havingValue = "true")
public class CatalogAutoConfiguration {

    @Bean
    public ApiInfoCollector apiInfoCollector() {
        return new ApiInfoCollector();
    }

    @Bean
    public ApiInfoController apiInfoController() {
        return new ApiInfoController();
    }

    @Bean
    public ApiInfoRunner apiInfoRunner() {
        return new ApiInfoRunner();
    }
}
