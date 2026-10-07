package com.coffeeordersystem.dataplatform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "data-platform")
public record DataPlatformProperties(
        String baseUrl,
        Duration connectTimeout,
        Duration readTimeout,
        Retry retry
) {

    public record Retry(boolean enabled, long fixedDelay, int maxCount, int batchSize) {
    }
}
