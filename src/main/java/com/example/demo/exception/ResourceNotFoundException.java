package com.example.demo.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String errorMessage;

    public ResourceNotFoundException(String message, String errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

}