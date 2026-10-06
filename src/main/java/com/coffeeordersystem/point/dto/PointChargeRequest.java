package com.coffeeordersystem.point.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointChargeRequest (
    @NotNull(message = "충전 금액은 필수입니다.")
    @Min(value = 1, message = "충전 금액은 1P 이상이어야 합니다.")
    @Max(value = 1_000_000, message = "1회 충전 금액은 1,000,000P 이하여야 합니다.")
    Long amount
) {
}
