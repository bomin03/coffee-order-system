package com.coffeeordersystem.order.domain;

public record OrderLine(Long menuId,  String menuName, long unitPrice, int quantity) {

    public long amount() {
        return unitPrice * quantity;
    }
}
