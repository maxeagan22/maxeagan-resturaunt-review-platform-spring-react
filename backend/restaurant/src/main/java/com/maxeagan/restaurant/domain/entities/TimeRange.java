package com.maxeagan.restaurant.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Represents a time range with opening and closing times.
 * Used for things like restaurant hours or availability windows.
 *
 * Notes:
 * - Both times are stored as strings (e.g., "09:00", "18:30").
 * - Indexed as keywords for exact match and filtering.
 * - Assumes 24-hour format.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeRange {

    /**
     * Opening time in 24-hour format (e.g., "09:00").
     * Stored as a keyword to allow exact match queries.
     */
    @Field(type = FieldType.Keyword)
    private String openTime;

    /**
     * Closing time in 24-hour format (e.g., "18:30").
     * Also stored as a keyword for precise filtering.
     */
    @Field(type = FieldType.Keyword)
    private String closeTime;
}
