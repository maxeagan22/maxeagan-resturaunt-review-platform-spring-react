package com.maxeagan.restaurant.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Represents a restaurant's weekly operating schedule.
 * Each day is mapped to a {@link TimeRange}, allowing for customizable hours.
 *
 * Notes:
 * - All fields are nested objects in Elasticsearch for structured queries.
 * - Days can be set to null to represent closures.
 * - Assumes a single open/close range per day; extend to List<TimeRange> for split shifts.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperatingHours {

    @Field(type = FieldType.Nested)
    private TimeRange monday;

    @Field(type = FieldType.Nested)
    private TimeRange tuesday;

    @Field(type = FieldType.Nested)
    private TimeRange wednesday;

    @Field(type = FieldType.Nested)
    private TimeRange thursday;

    @Field(type = FieldType.Nested)
    private TimeRange friday;

    @Field(type = FieldType.Nested)
    private TimeRange saturday;

    @Field(type = FieldType.Nested)
    private TimeRange sunday;
}
