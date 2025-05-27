package com.maxeagan.restaurant.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service interface for handling file storage operations.
 *
 * Responsibilities include storing files and retrieving them as resources.
 */
public interface StorageService {

    /**
     * Stores the given multipart file under the specified file name.
     *
     * @param file the file to store
     * @param fileName the target file name or identifier
     * @return the identifier or path of the stored file
     */
    String store(MultipartFile file, String fileName);

    /**
     * Loads a stored file as a Spring {@link Resource} by its identifier.
     *
     * @param id the unique identifier or file name of the stored file
     * @return an {@link Optional} containing the resource if found, or empty if not
     */
    Optional<Resource> loadAsResource(String id);
}

