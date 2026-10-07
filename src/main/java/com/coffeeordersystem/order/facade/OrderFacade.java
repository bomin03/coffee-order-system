package com.coffeeordersystem.order.facade;

import com.coffeeordersystem.global.lock.LockExecutor;
import com.coffeeordersystem.global.lock.LockKeys;
import com.coffeeordersystem.order.domain.OrderLine;
import com.coffeeordersystem.order.dto.OrderCreateRequest;
import com.coffeeordersystem.order.dto.OrderCreateResponse;
import com.coffeeordersystem.order.service.OrderService;
import com.coffeeordersystem.order.service.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final LockExecutor lockExecutor;
    private final OrderValidator orderValidator;
    private final OrderService orderService;

    public OrderCreateResponse placeOrder(OrderCreateRequest request) {
        List<OrderLine> lines = orderValidator.validateAndPrice(request);
        return lockExecutor.executeWithLock(
                LockKeys.point(request.userId()),
                () -> orderService.placeOrder(request.userId(), lines));
    }
}
