package com.metazion.essence.catalog;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("metazion.essence.catalog")
public class ApiConfigProperties {

    private String[] controllerPrefixes = {};

    public String[] getControllerPrefixes() {
        return controllerPrefixes;
    }

    public void setControllerPrefixes(String... controllerPrefixes) {
        this.controllerPrefixes = controllerPrefixes.clone();
    }
}
