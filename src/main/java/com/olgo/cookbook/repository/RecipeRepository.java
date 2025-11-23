package com.olgo.cookbook.repository;

import com.olgo.cookbook.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

}
