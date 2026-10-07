package com.coffeeordersystem.order.service;

import com.coffeeordersystem.global.exception.BusinessException;
import com.coffeeordersystem.global.exception.ErrorCode;
import com.coffeeordersystem.menu.domain.Menu;
import com.coffeeordersystem.menu.repository.MenuRepository;
import com.coffeeordersystem.order.domain.OrderLine;
import com.coffeeordersystem.order.dto.OrderCreateRequest;
import com.coffeeordersystem.order.dto.OrderItemRequest;
import com.coffeeordersystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderValidator {

    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    public List<OrderLine>  validateAndPrice(OrderCreateRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Set<Long> menuIds = new HashSet<>();
        for (OrderItemRequest item : request.items()) {
            if (!menuIds.add(item.menuId())) {
                throw new BusinessException(ErrorCode.DUPLICATE_MENU_IN_ORDER);
            }
        }

        Map<Long, Menu> menus = menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));
        if (menus.size() != menuIds.size()) {
            throw new BusinessException(ErrorCode.MENU_NOT_FOUND);
        }
        return request.items().stream()
                .map(item -> {
                    Menu menu = menus.get(item.menuId());
                    return new OrderLine(menu.getId(), menu.getName(), menu.getPrice(), item.quantity());
                })
                .toList();
    }
}
