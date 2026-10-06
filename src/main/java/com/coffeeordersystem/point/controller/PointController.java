package com.coffeeordersystem.point.controller;

import com.coffeeordersystem.point.dto.PointChargeRequest;
import com.coffeeordersystem.point.dto.PointChargeResponse;
import com.coffeeordersystem.point.facade.PointFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/{userId}/points")
@RequiredArgsConstructor
public class PointController {

    private final PointFacade pointFacade;

    @PostMapping("/charge")
    public ResponseEntity<PointChargeResponse> charge(
            @PathVariable Long userId,
            @Valid @RequestBody PointChargeRequest request) {
        return ResponseEntity.ok(pointFacade.charge(userId, request.amount()));
    }
}
