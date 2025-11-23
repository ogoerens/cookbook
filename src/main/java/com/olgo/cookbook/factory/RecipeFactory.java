package com.olgo.cookbook.factory;

import com.olgo.cookbook.dto.RecipeDto;
import com.olgo.cookbook.dto.RecipeIngredientDto;
import com.olgo.cookbook.model.Ingredient;
import com.olgo.cookbook.model.Recipe;
import com.olgo.cookbook.model.RecipeIngredient;
import com.olgo.cookbook.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeFactory {

    private final IngredientRepository ingredientRepo;

    public Recipe fromDto(final RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.name());
        recipe.setSteps(recipeDto.steps());

        Set<RecipeIngredient> recipeIngredients = recipeDto.ingredients()
                .stream()
                .map(dto -> toRecipeIngredient(dto, recipe))
                .collect(Collectors.toSet());

        recipe.setIngredients(recipeIngredients);
        return recipe;
    }

    private RecipeIngredient toRecipeIngredient(final RecipeIngredientDto recipeIngredientDto, final Recipe recipe) {
        final Ingredient ingredient = ingredientRepo
                .findByName(recipeIngredientDto.name())
                .orElseGet(() -> ingredientRepo.save(new Ingredient(recipeIngredientDto.name())));

        final RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(recipeIngredientDto.quantity());
        recipeIngredient.setUnit(recipeIngredientDto.unit());

        return recipeIngredient;
    }
}
