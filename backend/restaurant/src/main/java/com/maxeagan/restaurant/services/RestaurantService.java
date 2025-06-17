package com.maxeagan.restaurant.services;

import com.maxeagan.restaurant.domain.RestaurantCreateUpdateRequest;
import com.maxeagan.restaurant.domain.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for managing restaurant-related operations.
 */
public interface RestaurantService {

    /**
     * Creates a new {@link Restaurant} based on the provided creation/update request.
     * <p>
     * This typically includes:
     * <ul>
     *   <li>Resolving geolocation from the provided address</li>
     *   <li>Processing associated photo metadata</li>
     *   <li>Persisting the complete restaurant entity</li>
     * </ul>
     *
     * @param request a DTO containing restaurant details such as name, address, contact info, photos, and hours
     * @return the newly created and persisted {@link Restaurant} entity
     */
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);

    Page<Restaurant> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

    Optional<Restaurant> getRestaurant(String id);

    Restaurant updateRestaurant(String id, RestaurantCreateUpdateRequest restaurantCreateUpdateRequest);

    void deleteRestaurant(String id);
}
