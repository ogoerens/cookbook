package com.olgo.cookbook.service;

import com.olgo.cookbook.dto.CreateRecipeDto;
import com.olgo.cookbook.dto.RecipeDto;
import com.olgo.cookbook.exceptions.ForbiddenException;
import com.olgo.cookbook.exceptions.ResourceNotFoundException;
import com.olgo.cookbook.factory.RecipeFactory;
import com.olgo.cookbook.mapper.RecipeMapper;
import com.olgo.cookbook.model.Recipe;
import com.olgo.cookbook.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {
    final private RecipeRepository recipeRepo;
    final private RecipeFactory recipeFactory;
    final private RecipeMapper recipeMapper;

    public RecipeDto getRecipe(UUID recipeId, UUID userId) {
        Recipe recipe = recipeRepo.findById(recipeId).orElse(null);
        return recipeMapper.toDto(recipe, userId);
    }

    @Transactional
    public RecipeDto createRecipe(CreateRecipeDto createRecipeDto, UUID userId) {
        Recipe recipeEntity = recipeFactory.fromDto(createRecipeDto, userId);
        Recipe savedRecipe = recipeRepo.save(recipeEntity);
        return recipeMapper.toDto(savedRecipe);
    }

    @Transactional
    public RecipeDto updateRecipe(UUID recipeId, RecipeDto recipeDto, UUID userId) {
        Recipe recipeEntity = recipeRepo.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException(recipeId.toString()));

        if (!userId.equals(recipeEntity.getCreatedBy().getId())) {
            throw new ForbiddenException("Recipe does not belong to user.");
        }

        recipeMapper.updateRecipeFromDto(recipeDto, recipeEntity);
        return recipeMapper.toDto(recipeRepo.save(recipeEntity));
    }
}
