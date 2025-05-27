package com.maxeagan.restaurant.exceptions;

public class BaseException extends RuntimeException{
    public BaseException(String message) {
        super(message);
    }

    public BaseException() {
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
