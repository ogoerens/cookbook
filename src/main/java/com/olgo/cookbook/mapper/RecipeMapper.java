package com.olgo.cookbook.mapper;

import com.olgo.cookbook.dto.RecipeDto;
import com.olgo.cookbook.model.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RecipeIngredientMapper.class)
public interface RecipeMapper {
    RecipeDto toDto(Recipe recipe);
}
