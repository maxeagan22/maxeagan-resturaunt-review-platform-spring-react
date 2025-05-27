package com.maxeagan.restaurant.services.impl;

import com.maxeagan.restaurant.exceptions.StorageException;
import com.maxeagan.restaurant.services.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;

/**
 * Service for storing files on the local file system.
 * Handles file validation, storage, and retrieval as Spring Resources.
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    /**
     * Root directory path for file storage, configurable via application properties.
     */
    @Value("${app.storage.location:uploads}")
    private String storageLocation;

    private Path rootLocation;

    /**
     * Maximum allowed file size in bytes (5MB).
     */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Allowed file extensions for upload.
     */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

    /**
     * Initializes the storage directory. Creates it if it doesn't exist.
     * Throws StorageException if creation fails.
     */
    @PostConstruct
    public void init() {
        rootLocation = Paths.get(storageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootLocation);
            log.info("Storage directory initialized at {}", rootLocation);
        } catch (IOException e) {
            log.error("Failed to create storage directory", e);
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    /**
     * Stores a multipart file in the storage directory with a specified filename.
     * Validates file size, extension, and content type before saving.
     * @param file the uploaded multipart file
     * @param fileName desired name (without extension) for stored file
     * @return the final stored filename including extension
     * @throws StorageException if validation fails or file can't be saved
     */
    @Override
    public String store(MultipartFile file, String fileName) {
        if (file.isEmpty()) {
            throw new StorageException("Cannot save an empty file");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new StorageException("File too large. Max size is " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new StorageException("Invalid original filename");
        }

        String extension = Optional.ofNullable(StringUtils.getFilenameExtension(originalFilename))
                .map(String::toLowerCase)
                .orElseThrow(() -> new StorageException("Missing file extension"));

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new StorageException("Unsupported file type: " + extension);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new StorageException("Unsupported content type: " + contentType);
        }

        String finalFileName = fileName + "." + extension;
        Path destinationFile = rootLocation.resolve(finalFileName).normalize();

        if (!destinationFile.startsWith(rootLocation)) {
            throw new StorageException("Cannot store file outside the configured directory");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            log.info("Stored file: {}", finalFileName);
            return finalFileName;
        } catch (IOException e) {
            log.error("Failed to store file {}", finalFileName, e);
            throw new StorageException("Failed to store file", e);
        }
    }

    /**
     * Loads a stored file as a Spring Resource.
     * Returns an Optional containing the resource if it exists and is readable.
     * @param fileName the stored filename to load
     * @return Optional<Resource> wrapping the file resource or empty if not found/readable
     */
    @Override
    public Optional<Resource> loadAsResource(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return Optional.of(resource);
            } else {
                return Optional.empty();
            }
        } catch (MalformedURLException e) {
            log.warn("Could not read file: {}", fileName, e);
            return Optional.empty();
        }
    }
}
