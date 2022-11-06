package com.portfolio.apple.exception;

public class NotFoundByApiException extends RuntimeException {

    public NotFoundByApiException(String message) {
        super(message);
    }
}
