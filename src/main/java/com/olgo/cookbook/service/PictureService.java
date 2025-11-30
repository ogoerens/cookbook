package com.olgo.cookbook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

@Service
public class PictureService {
    private static final Logger logger = LoggerFactory.getLogger(PictureService.class);


    public static String detectPictureType(byte[] picture) {

        String detectedType;
        try {
            detectedType = URLConnection.guessContentTypeFromStream(
                    new ByteArrayInputStream(picture)
            );
        } catch (IOException e) {
            logger.error(e.getMessage());
            detectedType = null;
            return detectedType;
        }

        if (detectedType == null) {
            detectedType = "application/octet-stream";
        }
        return detectedType;
    }
}
