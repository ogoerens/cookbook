package com.olgo.cookbook.exceptions;

public class ValidationException extends RuntimeException {

    private final String failedValidation;

    public ValidationException(String failedValidation, String message) {
        super(message);
        this.failedValidation = failedValidation;
    }

    public String getFailedValidation() {
        return failedValidation;
    }
}
