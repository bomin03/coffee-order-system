package com.coffeeordersystem.global.lock;

public final class LockKeys {

    public static final String FAILED_EVENT_RESEND = "lock:scheduler:resend";

    private LockKeys() {
    }

    public static String point(Long userId){
        return "lock:point:" + userId;
    }
}
