package com.coffeeordersystem.point.domain;

import com.coffeeordersystem.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "point_history",
        indexes = @Index (name = "idx_point_history_user_created", columnList = "user_id, created_at"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PointHistoryType type;

    @Column(nullable = false)
    private long amount;

    @Column(name = "balance_after", nullable = false)
    private long balanceAfter;

    private PointHistory(Long userId, Long orderId, PointHistoryType type, long amount, long balanceAfter) {
        this.userId = userId;
        this.orderId = orderId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public static PointHistory charge(Long userId, long amount, long balanceAfter) {
        return new PointHistory(userId, null, PointHistoryType.CHARGE, amount, balanceAfter);
    }

    public static PointHistory use(Long userId, Long orderId, long amount, long balanceAfter) {
        return new PointHistory(userId, orderId, PointHistoryType.USE, amount, balanceAfter);
    }

}
