package com.olgo.cookbook.service;

import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.RecipeBookmarkPicture;
import com.olgo.cookbook.model.records.PictureData;
import com.olgo.cookbook.model.records.PictureMetadata;
import com.olgo.cookbook.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PictureService {
    private static final Logger logger = LoggerFactory.getLogger(PictureService.class);
    private final PictureRepository pictureRepository;

    public void addPictures(List<MultipartFile> pictures, RecipeBookmark bookmark) {
        for (int i = 0; i < pictures.size(); i++) {
            addPicture(pictures.get(i), bookmark, i);
        }
    }

    private void addPicture(MultipartFile picture, RecipeBookmark bookmark, int order) {
        if (picture == null || picture.isEmpty()) {
            throw new IllegalArgumentException("Picture cannot be empty");
        }
        byte[] pictureBytes;
        try {
            pictureBytes = picture.getBytes();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Failed to process picture", e);
        }
        RecipeBookmarkPicture picEntity = new RecipeBookmarkPicture();
        picEntity.setBookmark(bookmark);
        picEntity.setData(pictureBytes);
        picEntity.setContentType(detectPictureType(pictureBytes));
        picEntity.setSortOrder(order);
        bookmark.getPictures().add(picEntity);
    }

    @Transactional(readOnly = true)
    public List<PictureMetadata> getPictureMetadataForBookmark(RecipeBookmark bookmark) {

        return bookmark.getPictures().stream()
                .sorted(Comparator.comparingInt(RecipeBookmarkPicture::getSortOrder))
                .map(pic -> new PictureMetadata(
                        pic.getId(),
                        pic.getSortOrder()
                ))
                .toList();
    }


    public PictureData getPicture(UUID pictureId) {
        RecipeBookmarkPicture picture = pictureRepository.findById(pictureId).orElseThrow(
                () -> new IllegalArgumentException("Picture not found")
        );
        return new PictureData(picture.getData(), picture.getContentType());
    }

    public static String detectPictureType(byte[] picture) {

        String detectedType;
        try {
            detectedType = URLConnection.guessContentTypeFromStream(
                    new ByteArrayInputStream(picture)
            );
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Failed to detect picture type", e);
        }

        if (detectedType == null) {
            detectedType = "application/octet-stream";
        }
        return detectedType;
    }
}
