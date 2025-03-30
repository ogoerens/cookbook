package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.RecipeBookmarkRequest;
import com.olgo.cookbook.dto.responses.RecipeBookmarkResponse;
import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.RecipeBookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
public class RecipeBookmarkController {

    private final RecipeBookmarkService bookmarkService;

    public RecipeBookmarkController(RecipeBookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping
    public ResponseEntity<?> createBookmark(
            @AuthenticationPrincipal User user,
            @RequestBody RecipeBookmarkRequest request
    ) {
        RecipeBookmark saved = bookmarkService.createBookmark(
                user,
                request.getName(),
                request.getReferenceType(),
                request.getUrl(),
                request.getPicture(),
                request.getTags()
        );
        return ResponseEntity.ok("Bookmark created with ID: " + saved.getId());
    }

    @GetMapping
    public ResponseEntity<List<RecipeBookmarkResponse>> getAllBookmarks(@AuthenticationPrincipal User user) {
        List<RecipeBookmark> bookmarks = bookmarkService.getBookmarksForUser(user);
        var response = bookmarks.stream()
                .map(b -> new RecipeBookmarkResponse(
                        b.getId(),
                        b.getName(),
                        b.getReferenceType(),
                        b.getUrl(),
                        b.getTags().stream().map(Tag::getName).toList()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RecipeBookmarkResponse>> getBookmarksWithTags(
            @AuthenticationPrincipal User user,
            @RequestParam List<String> tags
    ) {
        var bookmarks = bookmarkService.getBookmarksForUserWithTags(user, tags);
        var response = bookmarks.stream()
                .map(b -> new RecipeBookmarkResponse(
                        b.getId(),
                        b.getName(),
                        b.getReferenceType(),
                        b.getUrl(),
                        b.getTags().stream().map(Tag::getName).toList()
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
}
