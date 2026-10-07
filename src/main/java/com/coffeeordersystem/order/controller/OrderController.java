package com.coffeeordersystem.order.controller;

import com.coffeeordersystem.order.dto.OrderCreateRequest;
import com.coffeeordersystem.order.dto.OrderCreateResponse;
import com.coffeeordersystem.order.facade.OrderFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> placeOrder(@Valid @RequestBody OrderCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderFacade.placeOrder(request));
    }
}