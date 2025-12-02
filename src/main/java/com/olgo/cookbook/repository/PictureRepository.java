package com.olgo.cookbook.repository;

import com.olgo.cookbook.model.RecipeBookmarkPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PictureRepository extends JpaRepository<RecipeBookmarkPicture, UUID> {
}
