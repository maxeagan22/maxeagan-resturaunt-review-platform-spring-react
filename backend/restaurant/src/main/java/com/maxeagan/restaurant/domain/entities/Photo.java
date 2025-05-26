package com.maxeagan.restaurant.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * Represents a photo associated with a restaurant (e.g., menu, storefront, interior).
 *
 * Notes:
 * - `url` stores a direct link to the image (can be CDN, S3, etc.).
 * - `uploadDate` tracks when the photo was added.
 * - Dates are stored in Elasticsearch.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Photo {

    /**
     * URL pointing to the photo resource.
     * Should be a complete, accessible path (e.g., public CDN or internal bucket link).
     */
    @Field(type = FieldType.Keyword)
    private String url;

    /**
     * Date and time the photo was uploaded.
     * Stored with `yyyy-MM-dd'T'HH:mm:ss` format in Elasticsearch.
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime uploadDate;
}
