package com.olgo.cookbook.mapper;

import com.olgo.cookbook.dto.RecipeIngredientDto;
import com.olgo.cookbook.model.RecipeIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeIngredientMapper {
    @Mapping(target = "name", source = "ingredient.name")
    RecipeIngredientDto toDto(RecipeIngredient recipeIngredient);
}
