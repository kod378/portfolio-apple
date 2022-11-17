package com.portfolio.apple.exception.category;

import com.portfolio.apple.exception.ApiEntityNotFoundException;

public class CategoryNotFoundException extends ApiEntityNotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
