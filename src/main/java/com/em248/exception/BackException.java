package com.em248.exception;

public class BackException extends RuntimeException {

    public BackException() {
    }

    public BackException(String message) {
        super(message);
    }

    public BackException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackException(Throwable cause) {
        super(cause);
    }

    public BackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
