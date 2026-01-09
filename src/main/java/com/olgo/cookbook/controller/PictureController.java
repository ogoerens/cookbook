package com.olgo.cookbook.controller;

import com.olgo.cookbook.model.records.PictureData;
import com.olgo.cookbook.model.records.SignedImageUrl;
import com.olgo.cookbook.useCase.ImageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pictures")

public class PictureController {
    private final ImageUseCase imageService;

    @GetMapping("{imageId}")
    public ResponseEntity<byte[]> getPicture(@PathVariable UUID imageId, @RequestParam long exp, @RequestParam String sig) {
        final PictureData picture = imageService.getImage(imageId, exp, sig);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(picture.contentType()))
                .header(HttpHeaders.CACHE_CONTROL, "private, max-age=300")
                .body(picture.bytes());
    }

    @GetMapping("/{imageId}/signed-url")
    public ResponseEntity<SignedImageUrl> getSignedUrl(@PathVariable UUID imageId) {
        SignedImageUrl signedImageUrl = imageService.getSignedImageUrl(imageId);

        return ResponseEntity.ok(signedImageUrl);
    }
}
