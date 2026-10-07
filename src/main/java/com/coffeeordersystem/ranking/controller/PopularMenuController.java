package com.coffeeordersystem.ranking.controller;

import com.coffeeordersystem.ranking.dto.PopularMenuResponse;
import com.coffeeordersystem.ranking.service.PopularMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class PopularMenuController {

    private final PopularMenuService popularMenuService;

    @GetMapping("/popular")
    public ResponseEntity<List<PopularMenuResponse>> getPopularMenu() {
        return ResponseEntity.ok(popularMenuService.getPopularMenus());
    }
}
