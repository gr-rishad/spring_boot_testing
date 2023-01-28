package com.javaguides.springboot.exception;

public class ResourceNotFundException extends RuntimeException {

    public ResourceNotFundException(String message) {
        super(message);
    }

    public ResourceNotFundException(String message, Throwable cause) {
        super(message, cause);
    }
}
