package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {
     private static final int MAX_ATTEMPTS = 2;  
    private static final int LOCK_TIME_MINUTES = 1;  
    private final ConcurrentHashMap<String, LoginAttempt> attemptsCache = new ConcurrentHashMap<>();

    
    public boolean isBlocked(String email) {
        LoginAttempt attempt = attemptsCache.get(email);
        if (attempt == null) {
            return false; 
        }

        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
         
            if (LocalDateTime.now().minusMinutes(LOCK_TIME_MINUTES).isBefore(attempt.getLastAttempt())) {
                return true; 
            } else {
               
                attemptsCache.remove(email);
            }
        }
        return false;
    }

   
    public void registerLoginAttempt(String email, boolean success) {
        LoginAttempt attempt = attemptsCache.get(email);
        if (attempt == null) {
            attempt = new LoginAttempt(email);
            attemptsCache.put(email, attempt);
        }

        if (success) {
            attempt.resetAttempts(); 
        } else {
            attempt.incrementAttempts();  
        }

        attempt.setLastAttempt(LocalDateTime.now()); 
    }

   
    private static class LoginAttempt {
        private final String email;
        private int attempts;
        private LocalDateTime lastAttempt;

        public LoginAttempt(String email) {
            this.email = email;
            this.attempts = 0;
            this.lastAttempt = LocalDateTime.now();
        }

        public void incrementAttempts() {
            this.attempts++;
        }

        public void resetAttempts() {
            this.attempts = 0;
        }

        public void setLastAttempt(LocalDateTime lastAttempt) {
            this.lastAttempt = lastAttempt;
        }

        public int getAttempts() {
            return attempts;
        }

        public LocalDateTime getLastAttempt() {
            return lastAttempt;
        }
    }
}

