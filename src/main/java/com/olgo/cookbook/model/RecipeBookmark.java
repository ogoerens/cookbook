package com.olgo.cookbook.model;

import com.olgo.cookbook.model.enums.ReferenceType;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @Lob
    @Column
    private byte[] picture;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "recipe_bm_tags",
            joinColumns = @JoinColumn(name = "recipe_boomkark_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public RecipeBookmark() {
    }

    // Optional constructor for convenience
    public RecipeBookmark(ReferenceType referenceType, String name, String url, byte[] picture, User user) {
        this.referenceType = referenceType;
        this.name = name;
        this.url = url;
        this.picture = picture;
        this.user = user;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public ReferenceType getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(ReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
