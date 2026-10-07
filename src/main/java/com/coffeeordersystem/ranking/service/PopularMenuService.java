package com.coffeeordersystem.ranking.service;

import com.coffeeordersystem.menu.domain.Menu;
import com.coffeeordersystem.menu.repository.MenuRepository;
import com.coffeeordersystem.ranking.dto.MenuOrderCount;
import com.coffeeordersystem.ranking.dto.PopularMenuResponse;
import com.coffeeordersystem.ranking.repository.PopularMenuQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PopularMenuService {

    static final int TOP_N = 3;
    static final int PERIOD_DAYS = 7;

    private final PopularMenuQueryRepository popularMenuQueryRepository;
    private final MenuRepository menuRepository;
    private final Clock clock;

    public List<PopularMenuResponse> getPopularMenus() {
        return getPopularMenus(LocalDate.now(clock));
    }

    public List<PopularMenuResponse> getPopularMenus(LocalDate today) {
        LocalDateTime from = today.minusDays(PERIOD_DAYS - 1).atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay();

        List<MenuOrderCount> counts =
                popularMenuQueryRepository.findTopMenus(from, to, PageRequest.of(0, TOP_N));
        if (counts.isEmpty()) {
            return List.of();
        }

        Map<Long, Menu> menus = menuRepository.findAllById(counts.stream().map(MenuOrderCount::menuId).toList())
                .stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));

        List<PopularMenuResponse> result = new ArrayList<>();
        for (MenuOrderCount count : counts) {
            Menu menu = menus.get(count.menuId());
            if (menu == null) {
                continue;
            }
            result.add(new PopularMenuResponse(result.size() + 1, menu.getId(), menu.getName(),
                    menu.getPrice(), count.orderCount()));
        }
        return result;
    }
}
