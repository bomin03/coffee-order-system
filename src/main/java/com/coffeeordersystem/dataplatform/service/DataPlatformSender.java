package com.coffeeordersystem.dataplatform.service;

import com.coffeeordersystem.dataplatform.client.DataPlatformClient;
import com.coffeeordersystem.dataplatform.client.DataPlatformPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPlatformSender {

    private final DataPlatformClient dataPlatformClient;
    private final FailedEventService failedEventService;

    public void send(DataPlatformPayload payload) {
        try {
            dataPlatformClient.send(payload);
        } catch (Exception e) {
            log.warn("수집 플랫폼 전송 실패. orderId={}, reason={}", payload.orderId(), e.getMessage());
            saveFailureSafely(payload.orderId(), e.getMessage());
        }
    }

    private void saveFailureSafely(Long orderId, String reason) {
        try {
            failedEventService.saveFailure(orderId, reason);
        } catch (Exception e) {
            log.error("전송 실패 기록 저장 실패. 수동 확인 필요. orderId={}", orderId, e);
        }
    }
}