package com.portfolio.apple.exception.category;

import com.portfolio.apple.exception.NotFoundByApiException;

public class CategoryNotFoundException extends NotFoundByApiException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
