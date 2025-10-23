package com.olgo.cookbook.repository;

import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeBookmarkRepository extends JpaRepository<RecipeBookmark, UUID> {

    List<RecipeBookmark> findAllByUser(User user);

    List<RecipeBookmark> findAllByUserId(UUID userid);

    List<RecipeBookmark> findByUserAndNameContainingIgnoreCase(User user, String name);

    Optional<RecipeBookmark> findByIdAndUserId(UUID id, UUID userId);


    @Query("""
                SELECT rb FROM RecipeBookmark rb
                JOIN rb.tags tag
                WHERE rb.user.id = :userId AND tag IN :tags
                GROUP BY rb
                HAVING COUNT(DISTINCT tag) = :tagCount
            """)
    List<RecipeBookmark> findByUserIdAndMatchingAllTags(
            @Param("userId") UUID userId,
            @Param("tags") List<Tag> tags,
            @Param("tagCount") long tagCount
    );

    @EntityGraph(attributePaths = "tags")
    @Query("select b from RecipeBookmark b where b.id = :id")
    Optional<RecipeBookmark> findByIdWithTags(UUID id);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);

}
