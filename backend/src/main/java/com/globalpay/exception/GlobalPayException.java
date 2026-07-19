package com.globalpay.exception;

public class GlobalPayException extends RuntimeException {
    public GlobalPayException(String message) {
        super(message);
    }

    public GlobalPayException(String message, Throwable cause) {
        super(message, cause);
    }
}
