package com.actech.uber.exception;

public abstract class UberException extends RuntimeException{
    public UberException(String message){
        super(message);
    }
}
