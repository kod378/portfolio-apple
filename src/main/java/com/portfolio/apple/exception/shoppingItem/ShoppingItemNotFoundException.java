package com.portfolio.apple.exception.shoppingItem;

import com.portfolio.apple.exception.ApiEntityNotFoundException;

public class ShoppingItemNotFoundException extends ApiEntityNotFoundException {
    public ShoppingItemNotFoundException(String message) {
        super(message);
    }
}
