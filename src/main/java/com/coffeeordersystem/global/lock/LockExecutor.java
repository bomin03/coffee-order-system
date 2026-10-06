package com.coffeeordersystem.global.lock;

import com.coffeeordersystem.global.exception.BusinessException;
import com.coffeeordersystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class LockExecutor {

    private static final long WAIT_SECONDS = 3;

    private final RedissonClient redissonClient;

    public <T> T executeWithLock(String key, Supplier<T> action) {
        RLock lock = redissonClient.getLock(key);
        try {
            if (!lock.tryLock(WAIT_SECONDS, TimeUnit.SECONDS)) {
                throw new BusinessException(ErrorCode.CONCURRENT_REQUEST);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.CONCURRENT_REQUEST);
        }

        try {
            return action.get();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public boolean executeIfAcquired(String key, Runnable action) {
        RLock lock = redissonClient.getLock(key);
        if (!lock.tryLock()) {
            return false;
        }
        try {
            action.run();
            return true;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
