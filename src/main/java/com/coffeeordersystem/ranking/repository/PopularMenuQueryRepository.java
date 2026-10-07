package com.coffeeordersystem.ranking.repository;

import com.coffeeordersystem.order.domain.OrderItem;
import com.coffeeordersystem.ranking.dto.MenuOrderCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PopularMenuQueryRepository extends Repository<OrderItem, Long> {

    @Query("""
            select new com.coffeeordersystem.ranking.dto.MenuOrderCount(oi.menuId, count(oi))
            from OrderItem oi
            where oi.orderedAt >= :from and oi.orderedAt < :to
            group by oi.menuId
            order by count(oi) desc, oi.menuId asc
            """)
    List<MenuOrderCount> findTopMenus(@Param("from") LocalDateTime from,
                                      @Param("to") LocalDateTime to,
                                      Pageable pageable);
}
