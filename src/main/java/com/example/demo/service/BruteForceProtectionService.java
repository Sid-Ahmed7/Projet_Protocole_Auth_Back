package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.Map;

@Service
public class BruteForceProtectionService {

    private static final int MAX_ATTEMPT = 5;
    private static final long LOCK_TIME = TimeUnit.MINUTES.toMillis(15);

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
        lockCache.remove(key); 
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            lockCache.put(key, System.currentTimeMillis()); 
        }
    }

    public boolean isBlocked(String key) {
        if (!lockCache.containsKey(key)) {
            return false;
        }

        long lockTime = lockCache.get(key);
        if (System.currentTimeMillis() - lockTime > LOCK_TIME) {
            lockCache.remove(key); 
            return false;
        }

        return true; 
    }
    public long getRemainingLockTime(String key) {
        if (!lockCache.containsKey(key)) {
            return 0;
        }
        long lockTime = lockCache.get(key);
        long elapsedTime = System.currentTimeMillis() - lockTime;
        long remainingTime = LOCK_TIME - elapsedTime;
        return remainingTime > 0 ? remainingTime / 1000 : 0; 
    }
    
}
