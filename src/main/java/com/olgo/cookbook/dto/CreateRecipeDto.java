package com.olgo.cookbook.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record CreateRecipeDto(
        UUID id,
        String name,
        Set<RecipeIngredientDto> ingredients,
        List<String> steps
) {
}

