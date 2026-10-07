package com.coffeeordersystem.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_item",
        uniqueConstraints = @UniqueConstraint(name = "uk_order_item_order_menu", columnNames = {"order_id", "menu_id"}),
        indexes = @Index(name = "idx_order_item_ordered_at_menu", columnList = "ordered_at, menu_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "menu_name", nullable = false, length = 50)
    private String menuName;

    @Column(name = "unit_price", nullable = false)
    private long unitPrice;

    @Column(nullable = false)
    private int quantity;

    /** 인기 메뉴 집계용 역정규화 컬럼. 주문의 orderedAt과 항상 같은 값으로 저장된다. */
    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    private OrderItem(Order order, OrderLine line, LocalDateTime orderedAt) {
        this.order = order;
        this.menuId = line.menuId();
        this.menuName = line.menuName();
        this.unitPrice = line.unitPrice();
        this.quantity = line.quantity();
        this.orderedAt = orderedAt;
    }

    static OrderItem of(Order order, OrderLine line, LocalDateTime orderedAt) {
        return new OrderItem(order, line, orderedAt);
    }

    public long getAmount() {
        return unitPrice * quantity;
    }
}