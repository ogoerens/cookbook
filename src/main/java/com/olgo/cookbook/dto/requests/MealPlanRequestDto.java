package com.olgo.cookbook.dto.requests;

import com.olgo.cookbook.model.enums.MealType;

import java.time.LocalDate;
import java.util.UUID;

public record MealPlanRequestDto(UUID recipeBookmarkId, LocalDate date, MealType mealType, boolean needsCooking) {
}
