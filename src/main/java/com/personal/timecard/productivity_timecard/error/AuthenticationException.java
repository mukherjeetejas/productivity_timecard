package com.personal.timecard.productivity_timecard.error;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}