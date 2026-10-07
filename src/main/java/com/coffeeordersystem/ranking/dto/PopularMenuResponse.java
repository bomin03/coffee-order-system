package com.coffeeordersystem.ranking.dto;

public record PopularMenuResponse(int rank, Long menuId, String name, long price, long orderCount) {
}