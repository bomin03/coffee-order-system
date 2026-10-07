package com.coffeeordersystem.order.domain;

import com.coffeeordersystem.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_price", nullable = false)
    private long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> orderItems = new ArrayList<>();

    private Order(Long userId, LocalDateTime orderedAt) {
        this.userId = userId;
        this.orderedAt = orderedAt;
        this.status = OrderStatus.PAID;
    }

    public static Order create(Long userId, List<OrderLine> lines, LocalDateTime orderedAt) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 1개 이상이어야 합니다.");
        }
        Order order = new Order(userId, orderedAt);
        for (OrderLine line : lines) {
            order.orderItems.add(OrderItem.of(order, line, orderedAt));
        }
        order.totalPrice = lines.stream().mapToLong(OrderLine::amount).sum();
        return order;
    }
}
