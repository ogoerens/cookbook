package com.olgo.cookbook.dto.responses;

import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.model.enums.ReferenceType;

import java.util.List;
import java.util.UUID;

public class RecipeBookmarkResponse {
    private UUID id;
    private String name;
    private ReferenceType referenceType;
    private String url;
    private List<String> tags;
    private String noteAddInfo;

    public RecipeBookmarkResponse(UUID id, String name, ReferenceType referenceType, String url, List<String> tags, String noteAddInfo) {
        this.id = id;
        this.name = name;
        this.referenceType = referenceType;
        this.url = url;
        this.tags = tags;
        this.noteAddInfo = noteAddInfo;
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

    public String getnoteAddInfo() {
        return noteAddInfo;
    }

    public static RecipeBookmarkResponse toDto(RecipeBookmark recipeBookmark) {
        return new RecipeBookmarkResponse(
                recipeBookmark.getId(),
                recipeBookmark.getName(),
                recipeBookmark.getReferenceType(),
                recipeBookmark.getUrl(),
                recipeBookmark.getTags().stream().map(Tag::getName).toList(),
                (recipeBookmark.getNote() == null ? null : recipeBookmark.getNote().getAdditionalInfo())
        );
    }
}
