package com.coffeeordersystem.order.repository;

import com.coffeeordersystem.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o join fetch o.orderItems where o.id = :orderId")
    Optional<Order> findWithItemsById(@Param("orderId") Long orderId);
}
