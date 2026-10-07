package com.coffeeordersystem.point.domain;

import com.coffeeordersystem.global.entity.BaseTimeEntity;
import com.coffeeordersystem.global.exception.BusinessException;
import com.coffeeordersystem.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_point",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_point_user", columnNames = "user_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPoint extends BaseTimeEntity {

    public static final long MAX_BALANCE = 10_000_000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private long balance;

    private UserPoint(Long userId) {
        this.userId = userId;
        this.balance = 0L;
    }

    public static UserPoint create(Long userId) {
        return new UserPoint(userId);
    }

    public void charge(long amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        if (balance + amount > MAX_BALANCE) {
            throw new BusinessException(ErrorCode.POINT_LIMIT_EXCEEDED);
        }
        this.balance += amount;
    }

    public void use(long amount) {
        if (amount <= 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
        if (balance < amount) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_POINT);
        }
        this.balance -= amount;
    }
}
