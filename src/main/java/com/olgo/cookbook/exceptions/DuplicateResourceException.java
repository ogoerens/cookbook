package com.olgo.cookbook.exceptions;

import com.olgo.cookbook.model.enums.ErrorCode;
import com.olgo.cookbook.model.records.ApiError;
import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {
    private final ApiError apiError;

    public DuplicateResourceException(String resource, String field) {
        super(resource + " already exists for " + field + ".");
        apiError = new ApiError(ErrorCode.DUPLICATE_RESOURCE.name(), "Resource already exists for " + field + ".");
    }
}
