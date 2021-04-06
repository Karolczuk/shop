package com.app.exception;

public class OrdersServiceException extends RuntimeException {
    public OrdersServiceException(String message) {
        super(message);
    }
}
