package com.olgo.cookbook.service;

import com.olgo.cookbook.dto.RecipeDto;
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

    public RecipeDto getRecipe(UUID id) {
        Recipe recipe = recipeRepo.findById(id).orElse(null);
        return recipeMapper.toDto(recipe);
    }

    @Transactional
    public RecipeDto createRecipe(RecipeDto recipeDto) {
        Recipe recipeEntity = recipeFactory.fromDto(recipeDto);
        Recipe savedRecipe = recipeRepo.save(recipeEntity);
        return recipeMapper.toDto(savedRecipe);
    }
}
