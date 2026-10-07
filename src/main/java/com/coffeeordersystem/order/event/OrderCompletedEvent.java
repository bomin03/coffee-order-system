package com.coffeeordersystem.order.event;

import com.coffeeordersystem.order.domain.Order;

import java.util.List;

public record OrderCompletedEvent(
        Long orderId,
        Long userId,
        long totalPrice,
        List<Item> items
) {
    public record Item(Long menuId, int quantity, long amount) {
    }

    public static OrderCompletedEvent from(Order order) {
        List<Item> items = order.getOrderItems().stream()
                .map(item -> new Item(item.getMenuId(), item.getQuantity(), item.getAmount()))
                .toList();
        return new OrderCompletedEvent(order.getId(), order.getUserId(), order.getTotalPrice(), items);
    }
}