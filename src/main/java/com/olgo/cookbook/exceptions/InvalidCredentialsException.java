package com.olgo.cookbook.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String loginIdentifier) {
        super("Invalid email or password provided. Email: " + loginIdentifier);
    }
}
