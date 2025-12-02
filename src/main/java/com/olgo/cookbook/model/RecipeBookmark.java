package com.olgo.cookbook.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olgo.cookbook.model.enums.ReferenceType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.*;

@Getter
@Entity
@Table(name = "recipe_bookmarks")
public class RecipeBookmark {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferenceType referenceType;

    @Column(nullable = false)
    private String name;

    @Column
    private String url;

    @OneToMany(
            mappedBy = "bookmark",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<RecipeBookmarkPicture> pictures = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "recipe_bm_tags",
            joinColumns = @JoinColumn(name = "recipe_boomkark_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_boomkark_id", "tag_id"})
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "recipeBookmark", cascade = CascadeType.ALL, orphanRemoval = true)
    private Note note;

    public RecipeBookmark() {
    }


    public RecipeBookmark(ReferenceType referenceType, String name, String url, User user, Note note) {
        this.referenceType = referenceType;
        this.name = name;
        this.url = url;
        this.user = user;
        this.setNote(note);
    }

    //  setters

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(ReferenceType referenceType) {
        this.referenceType = referenceType;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setNote(Note note) {
        if (note != null) {
            note.setRecipeBookmark(this);
            this.note = note;
        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
