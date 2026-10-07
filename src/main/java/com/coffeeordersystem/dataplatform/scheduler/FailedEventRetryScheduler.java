package com.coffeeordersystem.dataplatform.scheduler;

import com.coffeeordersystem.dataplatform.service.FailedEventRetryProcessor;
import com.coffeeordersystem.global.lock.LockExecutor;
import com.coffeeordersystem.global.lock.LockKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FailedEventRetryScheduler {

    private final LockExecutor lockExecutor;
    private final FailedEventRetryProcessor failedEventRetryProcessor;

    /** 모든 서버에서 실행되지만, 락을 잡은 한 서버만 재전송한다. */
    @Scheduled(fixedDelayString = "${data-platform.retry.fixed-delay}")
    public void retry() {
        boolean executed = lockExecutor.executeIfAcquired(
                LockKeys.FAILED_EVENT_RESEND,
                failedEventRetryProcessor::retryPending);
        if (!executed) {
            log.debug("다른 서버가 재전송 중이므로 이번 주기는 건너뜁니다.");
        }
    }
}
