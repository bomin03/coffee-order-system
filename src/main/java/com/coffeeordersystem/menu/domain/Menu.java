package com.coffeeordersystem.menu.domain;

import com.coffeeordersystem.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private long price;

    private Menu(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public static Menu create(String name, long price) {
        if (price <= 0) {
            throw new IllegalArgumentException("메뉴 가격은 0보다 커야 합니다.");
        }
        return new Menu(name, price);
    }
}
