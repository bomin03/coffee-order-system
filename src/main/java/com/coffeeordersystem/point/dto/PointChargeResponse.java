package com.coffeeordersystem.point.dto;

public record PointChargeResponse(Long userId, long chargedAmount, long balance) {
}
