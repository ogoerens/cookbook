package com.olgo.cookbook.dto.responses;

import com.olgo.cookbook.model.enums.ReferenceType;

import java.util.List;
import java.util.UUID;

public class RecipeBookmarkResponse {
    private UUID id;
    private String name;
    private ReferenceType referenceType;
    private String url;
    private List<String> tags;

    public RecipeBookmarkResponse(UUID id, String name, ReferenceType referenceType, String url, List<String> tags) {
        this.id = id;
        this.name = name;
        this.referenceType = referenceType;
        this.url = url;
        this.tags = tags;
    }

    // Getters only
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getTags() {
        return tags;
    }
}
