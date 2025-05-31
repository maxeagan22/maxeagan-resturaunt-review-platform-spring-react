package com.maxeagan.restaurant.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing a review left by a user for a restaurant.
 * <p>
 * Encapsulates review content, rating, timestamps, attached photos, and user metadata.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    /**
     * Unique identifier for the review.
     */
    private String id;

    /**
     * Textual content of the review.
     */
    private String content;

    /**
     * Numerical rating given by the user (typically 1–5).
     */
    private Integer rating;

    /**
     * Timestamp when the review was initially posted.
     */
    private LocalDateTime datePosted;

    /**
     * Timestamp when the review was last edited (if applicable).
     */
    private LocalDateTime lastEdited;

    /**
     * List of photos attached to the review.
     */
    private List<PhotoDto> photos = new ArrayList<>();

    /**
     * Information about the user who wrote the review.
     */
    private UserDto writtenBy;
}
