package com.elvira.core.config;

public enum Environment {

    LOCAL("https://local.site"),
    QA("https://qa.site"),
    STAGE("https://stage.site"),
    PROD("https://prod.site");

    private final String baseUrl;

    Environment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String baseUrl() {
        return baseUrl;
    }


    public static Environment current() {
        return Environment.valueOf(
                System.getProperty("env", "LOCAL")
                        .toUpperCase()
        );
    }
}
