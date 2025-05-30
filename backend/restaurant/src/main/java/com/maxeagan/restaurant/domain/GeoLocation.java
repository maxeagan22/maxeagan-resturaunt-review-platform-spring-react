package com.maxeagan.restaurant.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a geographic location using latitude and longitude coordinates.
 * <p>
 * Latitude and longitude are expressed as decimal degrees.
 * Typical usage includes mapping, geolocation services, and spatial calculations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeoLocation {
    /**
     * Latitude in decimal degrees. Positive values indicate north of the equator,
     * negative values indicate south.
     */
    private Double latitude;

    /**
     * Longitude in decimal degrees. Positive values indicate east of the Prime Meridian,
     * negative values indicate west.
     */
    private Double longitude;
}
