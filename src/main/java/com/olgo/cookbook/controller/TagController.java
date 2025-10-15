package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.UpdateTagsRequest;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookmark/{id}")
public class TagController {

    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PutMapping(path = "/tags")
    public ResponseEntity<?> updateTags(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user,
            @RequestBody UpdateTagsRequest request
    ) {
        tagService.replaceTagsForBookmark(id, request.getTagList());
        return ResponseEntity.noContent().build();
    }
}
