package com.portfolio.apple.exception;

public class EntityByIdNotFoundException extends RuntimeException {

    public EntityByIdNotFoundException(String message) {
        super(message);
    }
}
