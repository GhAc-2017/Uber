package com.actech.uber.exception;

public abstract class BookingException extends UberException{
    public BookingException(String message) {
        super(message);
    }
}
