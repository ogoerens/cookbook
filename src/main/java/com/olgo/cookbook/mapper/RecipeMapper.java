package com.olgo.cookbook.mapper;

import com.olgo.cookbook.dto.RecipeDto;
import com.olgo.cookbook.model.Recipe;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = RecipeIngredientMapper.class)
public interface RecipeMapper {

    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(target = "canEdit", constant = "false")
    RecipeDto toDto(Recipe recipe);

    @Mapping(source = "createdBy.id", target = "createdBy")
    @Mapping(target = "canEdit", expression = "java(canEdit(recipe, userId))")
    RecipeDto toDto(Recipe recipe, @Context UUID userId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "createdBy", ignore = true)
    void updateRecipeFromDto(RecipeDto recipeDto, @MappingTarget Recipe recipe);

    default boolean canEdit(Recipe recipe, UUID userId) {
        return recipe != null && recipe.getCreatedBy() != null && userId.equals(recipe.getCreatedBy().getId());
    }

    @AfterMapping
    default void linkIngredients(@MappingTarget Recipe recipe) {
        if (recipe.getIngredients() == null) return;
        recipe.getIngredients().forEach(ri -> ri.setRecipe(recipe));
    }
}
