package com.olgo.cookbook.mapper;

import com.olgo.cookbook.dto.RecipeDto;
import com.olgo.cookbook.model.Recipe;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = RecipeIngredientMapper.class)
public interface RecipeMapper {
    RecipeDto toDto(Recipe recipe);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecipeFromDto(RecipeDto recipeDto, @MappingTarget Recipe recipe);

    @AfterMapping
    default void linkIngredients(@MappingTarget Recipe recipe) {
        if (recipe.getIngredients() == null) return;
        recipe.getIngredients().forEach(ri -> ri.setRecipe(recipe));
    }
}
