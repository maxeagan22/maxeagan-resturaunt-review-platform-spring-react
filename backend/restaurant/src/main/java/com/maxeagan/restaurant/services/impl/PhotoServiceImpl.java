package com.maxeagan.restaurant.services.impl;

import com.maxeagan.restaurant.domain.entities.Photo;
import com.maxeagan.restaurant.services.PhotoService;
import com.maxeagan.restaurant.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of {@link PhotoService} that handles photo uploads and retrieval.
 *
 * Uses {@link StorageService} to persist and access photo files.
 */
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final StorageService storageService;

    /**
     * Uploads a photo by generating a unique ID, storing the file using {@link StorageService},
     * and returning a {@link Photo} entity containing the file URL and upload timestamp.
     *
     * @param file the image file to upload
     * @return a {@link Photo} entity with metadata for the uploaded image
     */
    @Override
    public Photo uploadPhoto(MultipartFile file) {
        String photoId = UUID.randomUUID().toString();
        String url = storageService.store(file, photoId);

        return Photo.builder()
                .url(url)
                .uploadDate(LocalDateTime.now())
                .build();
    }

    /**
     * Retrieves a previously uploaded photo as a {@link Resource} by its ID.
     *
     * @param id the unique identifier or file name of the stored photo
     * @return an {@link Optional} containing the resource if found, or empty if not
     */
    @Override
    public Optional<Resource> getPhotoAsResource(String id) {
        return storageService.loadAsResource(id);
    }
}
