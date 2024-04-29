package com.restaurantapi.exception;

public class ApiServiceException extends RuntimeException {

    public ApiServiceException() {
    }

    public ApiServiceException(String message) {
        super(message);
    }
}
