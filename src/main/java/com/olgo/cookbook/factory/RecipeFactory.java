package com.olgo.cookbook.factory;

import com.olgo.cookbook.dto.CreateRecipeDto;
import com.olgo.cookbook.dto.RecipeIngredientDto;
import com.olgo.cookbook.model.Ingredient;
import com.olgo.cookbook.model.Recipe;
import com.olgo.cookbook.model.RecipeIngredient;
import com.olgo.cookbook.repository.IngredientRepository;
import com.olgo.cookbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeFactory {

    private final IngredientRepository ingredientRepo;
    private final UserRepository userRepo;

    public Recipe fromDto(final CreateRecipeDto recipeDto, UUID userId) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.name());
        recipe.setSteps(recipeDto.steps());

        Set<RecipeIngredient> recipeIngredients = recipeDto.ingredients()
                .stream()
                .map(dto -> toRecipeIngredient(dto, recipe))
                .collect(Collectors.toSet());

        recipe.setIngredients(recipeIngredients);

        recipe.setCreatedBy(userRepo.getReferenceById(userId));
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
