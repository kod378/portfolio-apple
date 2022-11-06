package com.portfolio.apple.exception.item;

import com.portfolio.apple.exception.NotFoundByApiException;

public class ItemNotFoundException extends NotFoundByApiException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
