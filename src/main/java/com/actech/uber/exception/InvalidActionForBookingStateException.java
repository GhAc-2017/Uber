package com.actech.uber.exception;

public class InvalidActionForBookingStateException extends UberException {
    public InvalidActionForBookingStateException(String message) {
        super(message);
    }
}
