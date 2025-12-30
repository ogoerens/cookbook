package com.olgo.cookbook.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceId) {

        super("Resource with the following id not found: " + resourceId);
    }
}
