package com.coffeeordersystem.order.dto;

import com.coffeeordersystem.order.domain.OrderItem;

public record OrderItemResponse(Long menuId, String name, long unitPrice, int quantity) {

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(item.getMenuId(), item.getMenuName(), item.getUnitPrice(), item.getQuantity());
    }
}