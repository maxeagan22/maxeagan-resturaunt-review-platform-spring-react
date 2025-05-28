package com.maxeagan.restaurant.controllers;

import com.maxeagan.restaurant.domain.dtos.PhotoDto;
import com.maxeagan.restaurant.domain.entities.Photo;
import com.maxeagan.restaurant.mappers.PhotoMapper;
import com.maxeagan.restaurant.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing photo uploads and retrievals.
 * <p>
 * Exposes endpoints under <code>/api/photos</code> to handle multipart file uploads
 * and to return metadata about the uploaded images.
 * Uses {@link PhotoService} for storage operations and {@link PhotoMapper}
 * to convert internal {@link Photo} entities to external {@link PhotoDto} representations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    /**
     * Handles HTTP POST requests to upload a photo.
     * <p>
     * Expects a multipart form-data request with field name "file".
     * Delegates storage to {@link PhotoService#uploadPhoto(MultipartFile)} and
     * returns a {@link PhotoDto} containing the image URL and upload timestamp.
     *
     * @param file the image file to be uploaded (must be multipart/form-data)
     * @return a {@link PhotoDto} containing the stored photo's URL and upload date
     */
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public PhotoDto uploadPhoto(@RequestParam("file") MultipartFile file) {
        Photo savedPhoto = photoService.uploadPhoto(file);
        return photoMapper.toDto(savedPhoto);
    }
}
