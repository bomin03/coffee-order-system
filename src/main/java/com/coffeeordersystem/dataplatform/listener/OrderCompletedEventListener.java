package com.coffeeordersystem.dataplatform.listener;

import com.coffeeordersystem.dataplatform.client.DataPlatformPayload;
import com.coffeeordersystem.dataplatform.service.DataPlatformSender;
import com.coffeeordersystem.dataplatform.service.FailedEventService;
import com.coffeeordersystem.order.event.OrderCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Component
public class OrderCompletedEventListener {

    private final ThreadPoolTaskExecutor dataPlatformExecutor;
    private final DataPlatformSender dataPlatformSender;
    private final FailedEventService failedEventService;

    public OrderCompletedEventListener(@Qualifier("dataPlatformExecutor") ThreadPoolTaskExecutor dataPlatformExecutor,
                                       DataPlatformSender dataPlatformSender,
                                       FailedEventService failedEventService) {
        this.dataPlatformExecutor = dataPlatformExecutor;
        this.dataPlatformSender = dataPlatformSender;
        this.failedEventService = failedEventService;
    }

    /**
     * 주문 트랜잭션이 커밋된 뒤에만 실행된다. 롤백되면 호출되지 않는다.
     * 실제 전송은 전용 스레드 풀에 맡기고 즉시 반환하므로 주문 응답이 외부 시스템을 기다리지 않는다.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCompletedEvent event) {
        DataPlatformPayload payload = DataPlatformPayload.from(event);
        try {
            dataPlatformExecutor.execute(() -> dataPlatformSender.send(payload));
        } catch (RejectedExecutionException e) {
            log.warn("전송 작업 큐가 가득 차 실패로 기록합니다. orderId={}", event.orderId());
            try {
                failedEventService.saveFailure(event.orderId(), "executor rejected");
            } catch (Exception saveError) {
                log.error("전송 실패 기록 저장 실패. 수동 확인 필요. orderId={}", event.orderId(), saveError);
            }
        }
    }
}
