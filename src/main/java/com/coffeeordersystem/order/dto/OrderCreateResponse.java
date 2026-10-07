package com.coffeeordersystem.order.dto;


import com.coffeeordersystem.order.domain.Order;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateResponse(
        Long orderId,
        long totalPrice,
        long remainingPoint,
        LocalDateTime orderedAt,
        List<OrderItemResponse> items
) {

    public static OrderCreateResponse of(Order order, long remainingPoint) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .toList();
        return new OrderCreateResponse(order.getId(), order.getTotalPrice(), remainingPoint, order.getOrderedAt(), items);
    }
}
