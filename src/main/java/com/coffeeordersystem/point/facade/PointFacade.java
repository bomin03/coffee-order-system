package com.coffeeordersystem.point.facade;

import com.coffeeordersystem.global.exception.BusinessException;
import com.coffeeordersystem.global.exception.ErrorCode;
import com.coffeeordersystem.global.lock.LockExecutor;
import com.coffeeordersystem.global.lock.LockKeys;
import com.coffeeordersystem.point.dto.PointChargeResponse;
import com.coffeeordersystem.point.service.PointService;
import com.coffeeordersystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {

    private final LockExecutor lockExecutor;
    private final PointService pointService;
    private final UserRepository userRepository;

    public PointChargeResponse charge(Long userId, long amount) {
        if (!userRepository.existsById(userId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return lockExecutor.executeWithLock(
                LockKeys.point(userId),
                () -> pointService.charge(userId, amount));
    }
}