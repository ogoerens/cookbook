package com.olgo.cookbook.dto;

import com.olgo.cookbook.model.enums.Unit;

public record RecipeIngredientDto(
        String name,
        double quantity,
        Unit unit
) {
}
