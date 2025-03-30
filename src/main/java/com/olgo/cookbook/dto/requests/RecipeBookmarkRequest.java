package com.olgo.cookbook.dto.requests;

import com.olgo.cookbook.model.enums.ReferenceType;

import java.util.List;

public class RecipeBookmarkRequest {
    private String name;
    private ReferenceType referenceType;
    private String url;
    private byte[] picture;
    private List<String> tags;

    public RecipeBookmarkRequest() {
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(ReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
