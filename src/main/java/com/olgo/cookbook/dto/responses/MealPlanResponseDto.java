package com.olgo.cookbook.dto.responses;

import com.olgo.cookbook.model.enums.MealType;

import java.time.LocalDate;
import java.util.UUID;

public record MealPlanResponseDto(UUID id, UUID recipeBookmarkId, String bookmarkName, LocalDate date,
                                  MealType mealType, boolean needsCooking) {
}
