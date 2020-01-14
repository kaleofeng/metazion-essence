package com.metazion.essence.catalog.autoconfig;

import com.metazion.essence.catalog.ApiConfigProperties;
import com.metazion.essence.catalog.ApiInfoCollector;
import com.metazion.essence.catalog.controller.ApiInfoController;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiConfigProperties.class)
public class CatalogAutoConfiguration {

    @Bean
    public ApiInfoCollector apiInfoCollector() {
        return new ApiInfoCollector();
    }

    @Bean
    public ApiInfoController apiInfoController() {
        return new ApiInfoController();
    }
}
