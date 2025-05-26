package com.maxeagan.restaurant.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant entity indexed in Elasticsearch.
 * Captures identity, metadata, location, media, and user-generated content.
 *
 * Notes:
 * - Stored in the `restaurants` index.
 * - Supports full-text search on name and cuisineType.
 * - Includes nested data for address, hours, photos, reviews, and creator info.
 * - Geolocation field enables spatial queries.
 */
@Document(indexName = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant {

    /**
     * Unique restaurant identifier.
     */
    @Id
    private String id;

    /**
     * Restaurant name (full-text searchable).
     */
    @Field(type = FieldType.Text)
    private String name;

    /**
     * Cuisine category (e.g., Italian, Thai, BBQ).
     */
    @Field(type = FieldType.Text)
    private String cuisineType;

    /**
     * Contact info (e.g., phone number or email).
     * Stored as keyword for exact matching.
     */
    @Field(type = FieldType.Keyword)
    private String contactInformation;

    /**
     * Aggregated review score (nullable if unrated).
     */
    @Field(type = FieldType.Float)
    private Float averageRating;

    /**
     * Geolocation coordinates (used for spatial search).
     */
    @GeoPointField
    private GeoPoint geoLocation;

    /**
     * Full address details, including city/state/postalCode.
     */
    @Field(type = FieldType.Nested)
    private Address address;

    /**
     * Weekly operating hours for the restaurant.
     */
    @Field(type = FieldType.Nested)
    private OperatingHours operatingHours;

    /**
     * Optional media/photos representing the restaurant.
     */
    @Field(type = FieldType.Nested)
    private List<Photo> photos = new ArrayList<>();

    /**
     * List of user reviews with content, rating, and metadata.
     */
    @Field(type = FieldType.Nested)
    private List<Review> reviews = new ArrayList<>();

    /**
     * User who created/registered the restaurant.
     */
    @Field(type = FieldType.Nested)
    private User createdBy;
}
