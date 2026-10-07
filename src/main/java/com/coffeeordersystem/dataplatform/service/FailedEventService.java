package com.coffeeordersystem.dataplatform.service;

import com.coffeeordersystem.dataplatform.client.DataPlatformPayload;
import com.coffeeordersystem.dataplatform.domain.FailedEvent;
import com.coffeeordersystem.dataplatform.domain.FailedEventStatus;
import com.coffeeordersystem.dataplatform.repository.FailedEventRepository;
import com.coffeeordersystem.order.domain.Order;
import com.coffeeordersystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FailedEventService {

    private final FailedEventRepository failedEventRepository;
    private final OrderRepository orderRepository;

    /** 호출하는 쪽의 트랜잭션 상태와 무관하게, 항상 새 트랜잭션으로 실패 기록을 남긴다. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailure(Long orderId, String reason) {
        failedEventRepository.save(FailedEvent.create(orderId, reason));
    }

    @Transactional(readOnly = true)
    public List<FailedEvent> findPending(int size) {
        return failedEventRepository.findByStatusOrderByCreatedAtAsc(FailedEventStatus.PENDING, PageRequest.of(0, size));
    }

    /** 재전송 시 payload는 저장해 두지 않고 주문 원본에서 다시 만든다. */
    @Transactional(readOnly = true)
    public DataPlatformPayload loadPayload(Long orderId) {
        Order order = orderRepository.findWithItemsById(orderId)
                .orElseThrow(() -> new IllegalStateException("주문을 찾을 수 없습니다. orderId=" + orderId));
        return DataPlatformPayload.from(order);
    }

    @Transactional
    public void markSuccess(Long failedEventId) {
        failedEventRepository.findById(failedEventId).ifPresent(FailedEvent::markSuccess);
    }

    @Transactional
    public void recordRetryFailure(Long failedEventId, String reason, int maxRetryCount) {
        failedEventRepository.findById(failedEventId)
                .ifPresent(event -> event.recordRetryFailure(reason, maxRetryCount));
    }
}
