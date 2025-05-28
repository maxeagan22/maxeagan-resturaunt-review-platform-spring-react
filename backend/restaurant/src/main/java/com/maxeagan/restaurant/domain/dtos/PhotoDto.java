package com.maxeagan.restaurant.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a photo uploaded to the system.
 *
 * Used to expose photo metadata in API responses without exposing internal entity details.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDto {

    /**
     * Publicly accessible URL of the uploaded photo.
     */
    private String url;

    /**
     * Timestamp indicating when the photo was uploaded.
     */
    private LocalDateTime uploadDate;
}
