package com.coffeeordersystem.dataplatform.service;

import com.coffeeordersystem.dataplatform.client.DataPlatformClient;
import com.coffeeordersystem.dataplatform.config.DataPlatformProperties;
import com.coffeeordersystem.dataplatform.domain.FailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FailedEventRetryProcessor {

    private final FailedEventService failedEventService;
    private final DataPlatformClient dataPlatformClient;
    private final DataPlatformProperties properties;

    public void retryPending() {
        for (FailedEvent failedEvent : failedEventService.findPending(properties.retry().batchSize())) {
            try {
                dataPlatformClient.send(failedEventService.loadPayload(failedEvent.getOrderId()));
                failedEventService.markSuccess(failedEvent.getId());
            } catch (Exception e) {
                log.warn("재전송 실패. failedEventId={}, reason={}", failedEvent.getId(), e.getMessage());
                failedEventService.recordRetryFailure(failedEvent.getId(), e.getMessage(), properties.retry().maxCount());
            }
        }
    }
}
