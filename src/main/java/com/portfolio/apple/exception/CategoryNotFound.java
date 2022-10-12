package com.portfolio.apple.exception;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound(String message) {
        super(message);
    }
}
