package com.olgo.cookbook.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity
@Table(name = "notes")
public class Note implements Persistable<UUID> {
    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "id") // PK = FK
    private RecipeBookmark recipeBookmark;

    @Column(nullable = false)
    private String additionalInfo;

    @Transient
    private boolean isNew = true;

    public Note() {
    }

    public Note(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public UUID getId() {
        return id;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public RecipeBookmark getRecipeBookmark() {
        return recipeBookmark;
    }

    public void setRecipeBookmark(RecipeBookmark recipeBookmark) {
        this.recipeBookmark = recipeBookmark;
        this.id = (recipeBookmark != null ? recipeBookmark.getId() : null);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    public void markNotNew() {
        this.isNew = false;
    }
}
