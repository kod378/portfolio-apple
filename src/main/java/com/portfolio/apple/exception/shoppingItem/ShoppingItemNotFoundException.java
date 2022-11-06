package com.portfolio.apple.exception.shoppingItem;

import com.portfolio.apple.exception.NotFoundByApiException;

public class ShoppingItemNotFoundException extends NotFoundByApiException {
    public ShoppingItemNotFoundException(String message) {
        super(message);
    }
}
