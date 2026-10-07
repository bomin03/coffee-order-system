package com.coffeeordersystem.order.service;

import com.coffeeordersystem.order.domain.Order;
import com.coffeeordersystem.order.domain.OrderLine;
import com.coffeeordersystem.order.dto.OrderCreateResponse;
import com.coffeeordersystem.order.event.OrderCompletedEvent;
import com.coffeeordersystem.order.repository.OrderRepository;
import com.coffeeordersystem.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointService pointService;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public OrderCreateResponse placeOrder(Long userId, List<OrderLine> lines) {
        Order order = orderRepository.save(Order.create(userId, lines, LocalDateTime.now(clock)));

        long remainingPoint = pointService.use(userId, order.getId(), order.getTotalPrice());

        eventPublisher.publishEvent(OrderCompletedEvent.from(order));
        return OrderCreateResponse.of(order, remainingPoint);
    }
}
