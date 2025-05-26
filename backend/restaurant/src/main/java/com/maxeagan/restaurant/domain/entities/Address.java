package com.maxeagan.restaurant.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Represents an address used for a restaurant location.
 * Indexed in Elasticsearch for fast querying and filtering.
 *
 * Notes:
 * - All fields are annotated for Elasticsearch mapping.
 * - `Keyword` is used where exact match is needed (e.g., postal code, state).
 * - `Text` is used where full-text search may be useful (e.g., street name).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {

    /**
     * Street number of the address (e.g., "123").
     * Indexed as a keyword for exact matches.
     */
    @Field(type = FieldType.Keyword)
    private String streetNumber;

    /**
     * Street name (e.g., "Main St").
     * Indexed as text to allow partial/full-text search.
     */
    @Field(type = FieldType.Text)
    private String streetName;

    /**
     * Optional unit number (e.g., "Apt 4B", "Suite 202").
     * Indexed as a keyword.
     */
    @Field(type = FieldType.Keyword)
    private String unit;

    /**
     * City name (e.g., "Kansas City").
     * Keyword for exact match filtering.
     */
    @Field(type = FieldType.Keyword)
    private String city;

    /**
     * State or province (e.g., "Missouri", "Ontario").
     * Keyword for filtering and faceting.
     */
    @Field(type = FieldType.Keyword)
    private String state;

    /**
     * Postal or ZIP code (e.g., "94105").
     * Keyword for filtering and aggregation.
     */
    @Field(type = FieldType.Keyword)
    private String postalCode;

    /**
     * Country name or code (e.g., "United States", "Canada").
     * Keyword for exact match.
     */
    @Field(type = FieldType.Keyword)
    private String country;

}
