package com.portfolio.apple.exception;

public class ApiEntityNotFoundException extends RuntimeException {

    public ApiEntityNotFoundException(String message) {
        super(message);
    }
}
