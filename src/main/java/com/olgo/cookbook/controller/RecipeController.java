package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.RecipeDto;
import com.olgo.cookbook.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<UUID> createRecipe(@RequestBody RecipeDto recipeDto) {
        RecipeDto recipe = recipeService.createRecipe(recipeDto);
        return ResponseEntity.ok(recipe.id());
    }

    @GetMapping("{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable UUID id) {
        RecipeDto recipe = recipeService.getRecipe(id);
        return ResponseEntity.ok(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable UUID id, @RequestBody RecipeDto recipeDto) {
        RecipeDto recipe = recipeService.updateRecipe(id, recipeDto);
        return ResponseEntity.ok(recipe.id());
    }
}
