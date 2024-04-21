package com.example.demo.errorhandling.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super("Validation error: " + message);
    }
}
