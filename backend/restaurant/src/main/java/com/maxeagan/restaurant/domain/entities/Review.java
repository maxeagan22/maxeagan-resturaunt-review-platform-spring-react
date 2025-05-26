package com.maxeagan.restaurant.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user-submitted review for a restaurant.
 * Includes text content, rating, timestamps, optional photos, and author info.
 *
 * Notes:
 * - Stored in Elasticsearch and supports nested photo and user data for deep search.
 * - `rating` is assumed to be an integer scale (e.g., 1–5).
 * - `id` should be globally unique (UUID or DB-generated).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    /**
     * Unique identifier for the review.
     * Can be a UUID, MongoDB ObjectId, or any globally unique string.
     */
    @Field(type = FieldType.Keyword)
    private String id;

    /**
     * Main text content of the review.
     * Full-text indexed for search and relevance scoring.
     */
    @Field(type = FieldType.Text)
    private String content;

    /**
     * Numerical rating score (e.g., 1–5 stars).
     */
    @Field(type = FieldType.Integer)
    private Integer rating;

    /**
     * Timestamp for when the review was originally posted.
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime datePosted;

    /**
     * Timestamp for the last edit (can be same as datePosted if never updated).
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime lastEdited;

    /**
     * Optional list of photos attached to the review.
     * Stored as nested documents to allow structured queries.
     */
    @Field(type = FieldType.Nested)
    private List<Photo> photos = new ArrayList<>();

    /**
     * User who authored the review.
     * Nested object representing the review's author metadata.
     */
    @Field(type = FieldType.Nested)
    private User writtenBy;

}
