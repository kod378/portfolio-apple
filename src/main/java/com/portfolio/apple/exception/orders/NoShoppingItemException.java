package com.portfolio.apple.exception.orders;

public class NoShoppingItemException extends RuntimeException {
    public NoShoppingItemException(String message) {
        super(message);
    }
}
