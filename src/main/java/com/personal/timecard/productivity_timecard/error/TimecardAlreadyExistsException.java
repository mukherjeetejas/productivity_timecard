package com.personal.timecard.productivity_timecard.error;

public class TimecardAlreadyExistsException extends RuntimeException {
    public TimecardAlreadyExistsException(String message) {
        super(message);
    }
}