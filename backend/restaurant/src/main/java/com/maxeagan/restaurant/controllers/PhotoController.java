package com.maxeagan.restaurant.controllers;

import com.maxeagan.restaurant.domain.dtos.PhotoDto;
import com.maxeagan.restaurant.domain.entities.Photo;
import com.maxeagan.restaurant.mappers.PhotoMapper;
import com.maxeagan.restaurant.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
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

    /**
     * Retrieves a photo by its filename or ID and returns it as a downloadable resource.
     *
     * @param id the filename or unique identifier of the photo (can include file extensions).
     * @return ResponseEntity containing the photo as a Resource if found, or 404 Not Found if not.
     *
     * Notes:
     * - The path variable uses `{id:.+}` to allow filenames with extensions (e.g., `image.jpg`).
     * - Sets the Content-Type based on the file's type if detectable, otherwise defaults to `application/octet-stream`.
     * - Uses `Content-Disposition: inline` to display the image in-browser when possible.
     */
    @GetMapping(path = "/{id:.+}") // indicates one or more.
    public ResponseEntity<Resource> getPhoto(@PathVariable String id) {
        return photoService.getPhotoAsResource(id).map(photo ->
                ResponseEntity.ok()
                        .contentType(MediaTypeFactory.getMediaType(photo)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM)
                        )
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(photo)
        ).orElse(ResponseEntity.notFound().build());
    }
}

