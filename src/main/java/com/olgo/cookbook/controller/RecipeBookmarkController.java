package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.RecipeBookmarkRequest;
import com.olgo.cookbook.dto.responses.RecipeBookmarkResponse;
import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.JwtService;
import com.olgo.cookbook.service.RecipeBookmarkService;
import com.olgo.cookbook.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/bookmarks")
public class RecipeBookmarkController {

    private final RecipeBookmarkService bookmarkService;
    private final JwtService jwtService;

    public RecipeBookmarkController(RecipeBookmarkService bookmarkService, JwtService jwtService) {
        this.bookmarkService = bookmarkService;
        this.jwtService = jwtService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createBookmark(
            @AuthenticationPrincipal(expression = "id") UUID userId,
            @RequestPart("bookmark") RecipeBookmarkRequest request,
            @RequestPart(value = "pictures", required = false) List<MultipartFile> pictures
    ) throws IOException {
        byte[] pictureBytes = null;

        // TODO: handle all pcitures, not only the first one
        if (pictures != null && !pictures.isEmpty()) {
            pictureBytes = pictures.get(0).getBytes();
        }

        RecipeBookmark saved = bookmarkService.createBookmark(
                userId,
                request.getName(),
                request.getReferenceType(),
                request.getUrl(),
                pictureBytes,
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
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ) {
        byte[] picture = bookmarkService.getPictureData(id, user);

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(picture);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteBookmark(@PathVariable UUID id, HttpServletRequest request) {
        String token = RequestUtils.extractJwtFromRequest(request);
        UUID userId = UUID.fromString(jwtService.extractUserId(token));

        try {
            bookmarkService.deleteBookmark(id, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
