package com.olgo.cookbook.service;

import com.olgo.cookbook.dto.responses.RecipeBookmarkResponse;
import com.olgo.cookbook.model.Note;
import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.model.enums.ReferenceType;
import com.olgo.cookbook.repository.RecipeBookmarkRepository;
import com.olgo.cookbook.repository.TagRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeBookmarkService {

    private final EntityManager entityManager;
    private final RecipeBookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public RecipeBookmarkService(
            EntityManager entityManager,
            RecipeBookmarkRepository bookmarkRepository,
            TagRepository tagRepository
    ) {
        this.entityManager = entityManager;
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public RecipeBookmark createBookmark(
            UUID userId,
            String name,
            ReferenceType referenceType,
            String url,
            byte[] picture,
            List<String> tagNames,
            Note note
    ) {
        Set<Tag> tags = resolveTags(tagNames);
        User userRef = entityManager.getReference(User.class, userId);

        RecipeBookmark bookmark = new RecipeBookmark(
                referenceType,
                name,
                url,
                picture,
                userRef,
                note
        );
        bookmark.setTags(tags);

        return bookmarkRepository.save(bookmark);
    }

    @Transactional(readOnly = true)
    public List<RecipeBookmarkResponse> getBookmarksForUserId(UUID userId) {
        List<RecipeBookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);

        return bookmarks.stream()
                .map(RecipeBookmarkResponse::toDto)
                .toList();
    }

    public List<RecipeBookmark> getBookmarksForUserIdWithTags(UUID userId, List<String> tagNames) {
        Set<Tag> tags = new HashSet<>(resolveTags(tagNames));
        return bookmarkRepository.findByUserIdAndMatchingAllTags(userId, new ArrayList<>(tags), tags.size());
    }

    public RecipeBookmark getBookmarkById(UUID id) {
        return bookmarkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bookmark not found or unauthorized"));
    }

    private Set<Tag> resolveTags(List<String> tagNames) {
        return tagNames.stream()
                .map(name -> tagRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name.trim().toLowerCase())))
                ).collect(Collectors.toSet());
    }

    public byte[] getPictureData(UUID bookmarkId) {
        RecipeBookmark bookmark = getBookmarkById(bookmarkId);

        if (bookmark.getReferenceType() != ReferenceType.PICTURE) {
            throw new IllegalArgumentException("Bookmark is not of type PICTURE");
        }

        byte[] picture = bookmark.getPicture();
        if (picture == null || picture.length == 0) {
            throw new NoSuchElementException("Picture not found for this bookmark");
        }

        return picture;
    }

    public void deleteBookmark(UUID bookmarkId, UUID userId) {
        boolean exists = bookmarkRepository.existsByIdAndUserId(bookmarkId, userId);
        if (!exists) {
            throw new IllegalArgumentException("Bookmark not found or not owned by user");
        }

        bookmarkRepository.deleteById(bookmarkId);
    }
}