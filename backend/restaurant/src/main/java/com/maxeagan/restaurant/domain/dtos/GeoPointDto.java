package com.maxeagan.restaurant.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a geographic point using latitude and longitude.
 * <p>
 * Typically used for mapping or location-based querying of restaurants or reviews.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoPointDto {

    /**
     * Latitude component of the geographic location.
     */
    private Double latitude;

    /**
     * Longitude component of the geographic location.
     */
    private Double longitude;
}
