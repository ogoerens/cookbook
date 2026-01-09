package com.olgo.cookbook.service.image;

import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.RecipeBookmarkPicture;
import com.olgo.cookbook.model.records.PictureData;
import com.olgo.cookbook.model.records.PictureMetadata;
import com.olgo.cookbook.model.records.SignedImageUrl;
import com.olgo.cookbook.repository.PictureRepository;
import com.olgo.cookbook.service.SigningService;
import com.olgo.cookbook.useCase.ImageUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService implements ImageUseCase {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final SigningService signingService;
    private final PictureRepository pictureRepository;

    private static final int PICTURE_URL_TTL_S = 180;


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

    @Override
    public PictureData getImage(UUID imageId, long expiresAt, String signature) {
        if (!signingService.verify(imageId, expiresAt, signature)) {
            throw new IllegalArgumentException("Invalid signature");
        }
        return getImage(imageId);
    }

    private PictureData getImage(UUID pictureId) {
        RecipeBookmarkPicture picture = pictureRepository.findById(pictureId).orElseThrow(
                () -> new IllegalArgumentException("Picture not found")
        );
        return new PictureData(picture.getData(), picture.getContentType());
    }

    private static String detectPictureType(byte[] picture) {
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

    @Override
    public SignedImageUrl getSignedImageUrl(UUID imageId) {
        long expiresAt = Instant.now().plusSeconds(PICTURE_URL_TTL_S).getEpochSecond();
        String signature = signingService.signUuid(imageId, expiresAt);
        String requestTarget = String.format("/pictures/%s?exp=%s&sig=%s", imageId, expiresAt, signature);
        return new SignedImageUrl(requestTarget);
    }
}
