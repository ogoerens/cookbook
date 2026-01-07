package com.olgo.cookbook.mapper;

import com.olgo.cookbook.dto.RecipeIngredientDto;
import com.olgo.cookbook.model.Ingredient;
import com.olgo.cookbook.model.RecipeIngredient;
import com.olgo.cookbook.repository.IngredientRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RecipeIngredientMapper {

    protected IngredientRepository ingredientRepository;

    @Mapping(target = "name", source = "ingredient.name")
    public abstract RecipeIngredientDto toDto(RecipeIngredient recipeIngredient);

    @Mapping(target = "recipe", ignore = true)
    @Mapping(source = "name", target = "ingredient.name")
    public abstract RecipeIngredient toEntity(RecipeIngredientDto dto);

    @AfterMapping
    protected void linkIngredient(RecipeIngredientDto dto, @MappingTarget RecipeIngredient entity) {
        Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(dto.name().trim())
                .orElseGet(() -> ingredientRepository.save(new Ingredient(dto.name().trim())));
        entity.setIngredient(ingredient);
    }

    @Autowired
    public void setIngredientRepository(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
}
