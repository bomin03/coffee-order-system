package com.coffeeordersystem.point.service;

import com.coffeeordersystem.global.exception.BusinessException;
import com.coffeeordersystem.global.exception.ErrorCode;
import com.coffeeordersystem.point.domain.PointHistory;
import com.coffeeordersystem.point.domain.UserPoint;
import com.coffeeordersystem.point.dto.PointChargeResponse;
import com.coffeeordersystem.point.repository.PointHistoryRepository;
import com.coffeeordersystem.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointRepository  userPointRepository;
    private final PointHistoryRepository  pointHistoryRepository;

    @Transactional
    public PointChargeResponse charge(Long userId, long amount) {
        UserPoint userPoint = getUserPoint(userId);
        userPoint.charge(amount);
        pointHistoryRepository.save(PointHistory.charge(userId, amount, userPoint.getBalance()));
        return new PointChargeResponse(userId, amount, userPoint.getBalance());
    }

    @Transactional
    public long use(Long userId, Long orderId, long amount) {
        UserPoint userPoint = getUserPoint(userId);
        userPoint.use(amount);
        pointHistoryRepository.save(PointHistory.use(userId, orderId, amount, userPoint.getBalance()));
        return userPoint.getBalance();
    }

    private UserPoint getUserPoint(Long userId) {
        return userPointRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
