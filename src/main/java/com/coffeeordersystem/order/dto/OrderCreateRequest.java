package com.coffeeordersystem.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderCreateRequest(
        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        @NotEmpty(message = "주문 항목은 1개 이상이어야 합니다.")
        @Size(max = 20, message = "한 주문에 담을 수 있는 메뉴는 20종 이하입니다.")
        List<@Valid OrderItemRequest> items
) {
}
