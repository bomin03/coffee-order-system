package com.coffeeordersystem.menu.service;

import com.coffeeordersystem.menu.dto.MenuResponse;
import com.coffeeordersystem.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;현

    public List<MenuResponse> getMenus() {
        return menuRepository.findAll(Sort.by("id")).stream()
                .map(MenuResponse::from)
                .toList();
    }
}
