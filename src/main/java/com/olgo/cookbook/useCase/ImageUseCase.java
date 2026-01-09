package com.olgo.cookbook.useCase;

import com.olgo.cookbook.model.records.PictureData;
import com.olgo.cookbook.model.records.SignedImageUrl;

import java.util.UUID;

public interface ImageUseCase {
    SignedImageUrl getSignedImageUrl(UUID imageId);

    PictureData getImage(UUID imageId, long expiresAt, String signature);

}
