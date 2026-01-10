package com.olgo.cookbook.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table
@Getter
@Setter
public class Recipe {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeIngredient> ingredients = new HashSet<>();
    @ElementCollection
    @OrderColumn
    private List<String> steps = new ArrayList<>();

    @JoinColumn(name = "created_by_id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User createdBy;


}
