package com.portfolio.apple.exception.item;

import com.portfolio.apple.exception.ApiEntityNotFoundException;

public class ItemNotFoundException extends ApiEntityNotFoundException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
