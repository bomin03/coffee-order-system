package com.coffeeordersystem.dataplatform.repository;

import com.coffeeordersystem.dataplatform.domain.FailedEvent;
import com.coffeeordersystem.dataplatform.domain.FailedEventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FailedEventRepository extends JpaRepository<FailedEvent, Long> {

    /** idx_failed_event_status_created 인덱스를 타는 조회 */
    List<FailedEvent> findByStatusOrderByCreatedAtAsc(FailedEventStatus status, Pageable pageable);

    List<FailedEvent> findAllByOrderId(Long orderId);
}
