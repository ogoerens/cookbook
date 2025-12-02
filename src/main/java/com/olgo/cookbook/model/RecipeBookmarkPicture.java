package com.olgo.cookbook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class RecipeBookmarkPicture {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bookmark_id")
    private RecipeBookmark bookmark;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private int sortOrder;
}

