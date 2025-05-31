package com.maxeagan.restaurant.domain.dtos;

import com.maxeagan.restaurant.domain.entities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing a restaurant entity for external communication (e.g., API responses).
 * <p>
 * This class consolidates all major attributes of a restaurant, including identity, location, hours, photos, reviews, and metadata.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantDto {

    /**
     * Unique identifier for the restaurant.
     */
    private String id;

    /**
     * Name of the restaurant.
     */
    private String name;

    /**
     * Type of cuisine the restaurant serves (e.g., "Italian", "Mexican").
     */
    private String cuisineType;

    /**
     * Contact information for the restaurant (phone, email, etc.).
     */
    private String contactInformation;

    /**
     * Average customer rating for the restaurant.
     */
    private Float averageRating;

    /**
     * Geographical location of the restaurant (latitude and longitude).
     */
    private GeoPointDto geoLocation;

    /**
     * Physical address of the restaurant.
     */
    private AddressDto address;

    /**
     * Weekly operating hours for the restaurant.
     */
    private OperatingHoursDto operatingHours;

    /**
     * List of associated photo metadata.
     */
    private List<PhotoDto> photos = new ArrayList<>();

    /**
     * List of customer reviews for the restaurant.
     */
    private List<ReviewDto> reviews = new ArrayList<>();

    /**
     * Information about the user who created the restaurant entry.
     */
    private UserDto createdBy;
}
