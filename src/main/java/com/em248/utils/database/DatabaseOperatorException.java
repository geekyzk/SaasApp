package com.em248.utils.database;

public class DatabaseOperatorException extends RuntimeException {


    public DatabaseOperatorException() {
    }

    public DatabaseOperatorException(String message) {
        super(message);
    }

    public DatabaseOperatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseOperatorException(Throwable cause) {
        super(cause);
    }

    public DatabaseOperatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
