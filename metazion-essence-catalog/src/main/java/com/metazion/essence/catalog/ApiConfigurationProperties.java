package com.metazion.essence.catalog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("metazion.essence.catalog")
public class ApiConfigurationProperties {

    @Value("${controllerIncludePatterns:}")
    private String[] controllerIncludePatterns;

    @Value("${controllerExcludePatterns:}")
    private String[] controllerExcludePatterns;

    public String[] getControllerIncludePatterns() {
        return controllerIncludePatterns.clone();
    }

    public void setControllerIncludePatterns(String... controllerIncludePatterns) {
        this.controllerIncludePatterns = controllerIncludePatterns.clone();
    }

    public String[] getControllerExcludePatterns() {
        return controllerExcludePatterns.clone();
    }

    public void setControllerExcludePatterns(String... controllerExcludePatterns) {
        this.controllerExcludePatterns = controllerExcludePatterns.clone();
    }
}
