package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.RecipeBookmarkRequest;
import com.olgo.cookbook.dto.responses.RecipeBookmarkResponse;
import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.model.records.PictureData;
import com.olgo.cookbook.model.records.PictureMetadata;
import com.olgo.cookbook.service.JwtService;
import com.olgo.cookbook.service.RecipeBookmarkService;
import com.olgo.cookbook.service.image.ImageService;
import com.olgo.cookbook.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class RecipeBookmarkController {

    private final RecipeBookmarkService bookmarkService;
    private final JwtService jwtService;
    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBookmark(
            @AuthenticationPrincipal(expression = "id") UUID userId,
            @RequestPart("bookmark") RecipeBookmarkRequest request,
            @RequestPart(value = "pictures", required = false) List<MultipartFile> pictures
    ) throws IOException {

        List<MultipartFile> safePictures = (pictures != null) ? pictures : List.of();


        RecipeBookmark saved = bookmarkService.createBookmark(
                userId,
                request.getName(),
                request.getUrl(),
                safePictures,
                request.getTags(),
                request.getNote()
        );
        return ResponseEntity.ok(RecipeBookmarkResponse.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<RecipeBookmarkResponse>> getAllMyBookmarks(@AuthenticationPrincipal(expression = "id") UUID userId) {
        List<RecipeBookmarkResponse> response = bookmarkService.getBookmarksForUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecipeBookmarkResponse>> getAllBookmarksForUser(@PathVariable UUID userId) {
        List<RecipeBookmarkResponse> response = bookmarkService.getBookmarksForUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RecipeBookmarkResponse>> getBookmarksWithTags(
            @AuthenticationPrincipal(expression = "id") UUID userId,
            @RequestParam List<String> tags
    ) {
        var bookmarks = bookmarkService.getBookmarksForUserIdWithTags(userId, tags);
        List<RecipeBookmarkResponse> response = bookmarks.stream()
                .map(b -> new RecipeBookmarkResponse(
                        b.getId(),
                        b.getName(),
                        b.getReferenceType(),
                        b.getUrl(),
                        b.getTags().stream().map(Tag::getName).toList(),
                        (b.getNote() == null ? null : b.getNote().getAdditionalInfo())
                ))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/picture")
    public ResponseEntity<byte[]> getPictureForBookmark(
            @PathVariable UUID id
    ) {
        PictureData picture = bookmarkService.getPictureData(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(picture.contentType()))
                .body(picture.bytes());
    }

    @GetMapping("/{id}/pictures")
    public ResponseEntity<List<PictureMetadata>> getPicturesForBookmark(
            @PathVariable("id") UUID bookmarkId
    ) {
        RecipeBookmark bookmark = bookmarkService.getBookmarkById(bookmarkId);
        List<PictureMetadata> pictureMetadata = imageService.getPictureMetadataForBookmark(bookmark);

        return ResponseEntity.ok(pictureMetadata);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookmark(@AuthenticationPrincipal(expression = "id") UUID userId, @PathVariable UUID id, @RequestBody RecipeBookmarkRequest requestBody
    ) {
        bookmarkService.updateBookmark(id, userId, requestBody);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteBookmark(@PathVariable UUID id, HttpServletRequest request) {
        String token = RequestUtils.extractJwt(request);
        UUID userId = UUID.fromString(jwtService.extractUserId(token));

        try {
            bookmarkService.deleteBookmark(id, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
