package com.coffeeordersystem.dataplatform.domain;

import com.coffeeordersystem.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "failed_event",
        indexes = @Index(name = "idx_failed_event_status_created", columnList = "status, created_at"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FailedEvent extends BaseTimeEntity {

    private static final int MAX_ERROR_LENGTH = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FailedEventStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "last_error", length = MAX_ERROR_LENGTH)
    private String lastError;

    private FailedEvent(Long orderId, String lastError) {
        this.orderId = orderId;
        this.status = FailedEventStatus.PENDING;
        this.retryCount = 0;
        this.lastError = truncate(lastError);
    }

    public static FailedEvent create(Long orderId, String reason) {
        return new FailedEvent(orderId, reason);
    }

    public void markSuccess() {
        this.status = FailedEventStatus.SUCCESS;
    }

    public void recordRetryFailure(String reason, int maxRetryCount) {
        this.retryCount++;
        this.lastError = truncate(reason);
        if (retryCount >= maxRetryCount) {
            this.status = FailedEventStatus.FAILED;
        }
    }

    private static String truncate(String value) {
        if (value == null) {
            return null;
        }
        return value.length() <= MAX_ERROR_LENGTH ? value : value.substring(0, MAX_ERROR_LENGTH);
    }
}
