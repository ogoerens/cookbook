package com.olgo.cookbook.service;

import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.model.enums.ReferenceType;
import com.olgo.cookbook.repository.RecipeBookmarkRepository;
import com.olgo.cookbook.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeBookmarkService {

    private final RecipeBookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;

    public RecipeBookmarkService(
            RecipeBookmarkRepository bookmarkRepository,
            TagRepository tagRepository
    ) {
        this.bookmarkRepository = bookmarkRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public RecipeBookmark createBookmark(
            User user,
            String name,
            ReferenceType referenceType,
            String url,
            byte[] picture,
            List<String> tagNames
    ) {
        Set<Tag> tags = resolveTags(tagNames);

        RecipeBookmark bookmark = new RecipeBookmark(
                referenceType,
                name,
                url,
                picture,
                user
        );
        bookmark.setTags(tags);

        return bookmarkRepository.save(bookmark);
    }

    public List<RecipeBookmark> getBookmarksForUser(User user) {
        return bookmarkRepository.findAllByUser(user);
    }

    public List<RecipeBookmark> getBookmarksForUserWithTags(User user, List<String> tagNames) {
        Set<Tag> tags = new HashSet<>(resolveTags(tagNames));
        return bookmarkRepository.findByUserAndMatchingAllTags(user, new ArrayList<>(tags), tags.size());
    }

    public RecipeBookmark getBookmarkByIdForUser(UUID id, User user) {
        return bookmarkRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Bookmark not found or unauthorized"));
    }

    private Set<Tag> resolveTags(List<String> tagNames) {
        return tagNames.stream()
                .map(name -> tagRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name.trim().toLowerCase())))
                ).collect(Collectors.toSet());
    }

    public byte[] getPictureData(UUID bookmarkId, User user) {
        RecipeBookmark bookmark = getBookmarkByIdForUser(bookmarkId, user);

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