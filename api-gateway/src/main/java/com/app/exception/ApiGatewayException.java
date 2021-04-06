package com.app.exception;

public class ApiGatewayException extends RuntimeException {
    public ApiGatewayException(String message) {
        super(message);
    }
}
