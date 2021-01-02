package com.actech.uber.exception;

public class InvalidActionForBookingStateException extends BookingException {
    public InvalidActionForBookingStateException(String message) {
        super(message);
    }
}
