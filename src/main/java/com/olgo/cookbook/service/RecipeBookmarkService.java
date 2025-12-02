package com.olgo.cookbook.service;

import com.olgo.cookbook.dto.responses.RecipeBookmarkResponse;
import com.olgo.cookbook.model.*;
import com.olgo.cookbook.model.enums.ReferenceType;
import com.olgo.cookbook.model.records.PictureData;
import com.olgo.cookbook.repository.RecipeBookmarkRepository;
import com.olgo.cookbook.repository.TagRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RecipeBookmarkService {

    private final EntityManager entityManager;
    private final RecipeBookmarkRepository bookmarkRepository;
    private final TagRepository tagRepository;
    private final PictureService pictureService;

    @Transactional
    public RecipeBookmark createBookmark(
            UUID userId,
            String name,
            String url,
            List<MultipartFile> pictures,
            List<String> tagNames,
            Note note
    ) {
        ReferenceType referenceType = pictures.isEmpty()
                ? ReferenceType.URL
                : ReferenceType.PICTURE;

        Set<Tag> tags = resolveTags(tagNames);
        User userRef = entityManager.getReference(User.class, userId);

        RecipeBookmark bookmark = new RecipeBookmark(
                referenceType,
                name,
                url,
                userRef,
                note
        );
        bookmark.setTags(tags);
        pictureService.addPictures(pictures, bookmark);

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

    public PictureData getPictureData(UUID bookmarkId) {
        RecipeBookmark bookmark = getBookmarkById(bookmarkId);

        if (bookmark.getReferenceType() != ReferenceType.PICTURE) {
            throw new IllegalArgumentException("Bookmark is not of type PICTURE");
        }

        RecipeBookmarkPicture picture = bookmark.getPictures().stream()
                .min(Comparator.comparingInt(RecipeBookmarkPicture::getSortOrder))
                .orElseThrow(() -> new NoSuchElementException("Picture not found for this bookmark"));

        return new PictureData(picture.getData(), picture.getContentType());
    }

    public void deleteBookmark(UUID bookmarkId, UUID userId) {
        boolean exists = bookmarkRepository.existsByIdAndUserId(bookmarkId, userId);
        if (!exists) {
            throw new IllegalArgumentException("Bookmark not found or not owned by user");
        }

        bookmarkRepository.deleteById(bookmarkId);
    }
}