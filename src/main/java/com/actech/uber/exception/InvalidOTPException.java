package com.actech.uber.exception;

public class InvalidOTPException extends UberException {
    public InvalidOTPException() {
        super("Invalid OTP");
    }
}
