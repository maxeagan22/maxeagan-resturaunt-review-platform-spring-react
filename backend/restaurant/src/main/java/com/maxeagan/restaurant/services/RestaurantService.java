package com.maxeagan.restaurant.services;

import com.maxeagan.restaurant.domain.RestaurantCreateUpdateRequest;
import com.maxeagan.restaurant.domain.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for managing restaurant-related operations.
 * Handles business logic for CRUD and search functionality.
 */
public interface RestaurantService {

    /**
     * Creates a new {@link Restaurant} based on the provided input.
     * <p>
     * Handles operations such as:
     * <ul>
     *   <li>Geolocation resolution</li>
     *   <li>Photo metadata processing</li>
     *   <li>Persistence of the restaurant entity</li>
     * </ul>
     *
     * @param request the input data for creating a restaurant
     * @return the persisted {@link Restaurant} entity
     */
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);

    /**
     * Searches for restaurants based on optional filters like text query, rating,
     * and location radius. Supports pagination.
     *
     * @param query     optional free-text query
     * @param minRating optional minimum rating filter
     * @param latitude  optional latitude for geo-based filtering
     * @param longitude optional longitude for geo-based filtering
     * @param radius    optional radius in kilometers
     * @param pageable  pagination and sorting configuration
     * @return a paginated list of matching restaurants
     */
    Page<Restaurant> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

    /**
     * Retrieves a restaurant by its unique ID.
     *
     * @param id the restaurant ID
     * @return an {@link Optional} containing the {@link Restaurant} if found
     */
    Optional<Restaurant> getRestaurant(String id);

    /**
     * Updates an existing restaurant with new data.
     *
     * @param id      the ID of the restaurant to update
     * @param request the update request data
     * @return the updated {@link Restaurant}
     */
    Restaurant updateRestaurant(String id, RestaurantCreateUpdateRequest request);

    /**
     * Deletes a restaurant by its ID.
     *
     * @param id the ID of the restaurant to delete
     */
    void deleteRestaurant(String id);
}
