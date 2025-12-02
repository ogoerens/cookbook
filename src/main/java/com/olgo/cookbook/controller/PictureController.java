package com.olgo.cookbook.controller;

import com.olgo.cookbook.model.records.PictureData;
import com.olgo.cookbook.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pictures")

public class PictureController {
    private final PictureService pictureService;

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getPicture(@PathVariable UUID id) {
        final PictureData picture = pictureService.getPicture(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(picture.contentType()))
                .body(picture.bytes());
    }

}
