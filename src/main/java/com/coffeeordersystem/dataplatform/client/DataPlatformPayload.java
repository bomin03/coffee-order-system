package com.coffeeordersystem.dataplatform.client;

import com.coffeeordersystem.order.domain.Order;
import com.coffeeordersystem.order.event.OrderCompletedEvent;

import java.util.List;

public record DataPlatformPayload(Long orderId, Long userId, long totalPrice, List<Item> items) {

    public record Item(Long menuId, int quantity, long amount) {
    }

    public static DataPlatformPayload from(OrderCompletedEvent event) {
        List<Item> items = event.items().stream()
                .map(item -> new Item(item.menuId(), item.quantity(), item.amount()))
                .toList();
        return new DataPlatformPayload(event.orderId(), event.userId(), event.totalPrice(), items);
    }

    public static DataPlatformPayload from(Order order){
        List<Item> items = order.getOrderItems().stream()
                .map(item -> new Item(item.getMenuId(), item.getQuantity(), item.getAmount()))
                .toList();
        return new DataPlatformPayload(order.getId(), order.getUserId(), order.getTotalPrice(), items);
    }
}
