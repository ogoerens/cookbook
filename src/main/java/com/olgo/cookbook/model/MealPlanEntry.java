package com.olgo.cookbook.model;


import com.olgo.cookbook.model.enums.MealType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "meal_plan_entries")
public class MealPlanEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private MealType mealType;

    @Column(nullable = false)
    private boolean needsCooking;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id", nullable = false)
    private RecipeBookmark bookmark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public MealPlanEntry() {
    }

    public MealPlanEntry(LocalDate plannedDate, MealType mealType, boolean needsCooking, RecipeBookmark recipe, User user) {
        this.date = plannedDate;
        this.mealType = mealType;
        this.needsCooking = needsCooking;
        this.bookmark = recipe;
        this.user = user;
    }
}