package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.CreateRecipeDto;
import com.olgo.cookbook.dto.RecipeDto;
import com.olgo.cookbook.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<UUID> createRecipe(@AuthenticationPrincipal(expression = "id") UUID userId, @RequestBody CreateRecipeDto recipeDto) {
        RecipeDto recipe = recipeService.createRecipe(recipeDto, userId);
        return ResponseEntity.ok(recipe.id());
    }

    @GetMapping("{recipeId}")
    public ResponseEntity<RecipeDto> getRecipe(@AuthenticationPrincipal(expression = "id") UUID userId, @PathVariable UUID recipeId) {
        RecipeDto recipe = recipeService.getRecipe(recipeId, userId);
        return ResponseEntity.ok(recipe);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<?> updateRecipe(@AuthenticationPrincipal(expression = "id") UUID userId, @PathVariable UUID recipeId, @RequestBody RecipeDto recipeDto) {
        RecipeDto recipe = recipeService.updateRecipe(recipeId, recipeDto, userId);
        return ResponseEntity.ok(recipe.id());
    }
}
