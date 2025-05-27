package com.maxeagan.restaurant.services;

import com.maxeagan.restaurant.domain.entities.Photo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service interface for handling photo-related operations.
 *
 * Manages uploading and retrieving photo resources tied to entities like reviews or restaurants.
 */
public interface PhotoService {

    /**
     * Uploads a photo file and returns a {@link Photo} object containing metadata like URL and upload date.
     *
     * @param file the image file to upload
     * @return a {@link Photo} object representing the uploaded image
     */
    Photo uploadPhoto(MultipartFile file);

    /**
     * Retrieves a stored photo as a Spring {@link Resource} by its identifier.
     *
     * @param id the unique ID or filename of the stored photo
     * @return an {@link Optional} containing the photo resource if found, or empty if not
     */
    Optional<Resource> getPhotoAsResource(String id);
}
