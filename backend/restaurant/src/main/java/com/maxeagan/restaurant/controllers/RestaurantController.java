package com.maxeagan.restaurant.controllers;

import com.maxeagan.restaurant.domain.RestaurantCreateUpdateRequest;
import com.maxeagan.restaurant.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.maxeagan.restaurant.domain.dtos.RestaurantDto;
import com.maxeagan.restaurant.domain.dtos.RestaurantSummaryDto;
import com.maxeagan.restaurant.domain.entities.Restaurant;
import com.maxeagan.restaurant.mappers.RestaurantMapper;
import com.maxeagan.restaurant.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling restaurant-related operations.
 * Supports CRUD actions and search functionality.
 */
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    /**
     * Creates a new restaurant.
     *
     * @param request DTO containing restaurant creation data
     * @return {@link ResponseEntity} with the created {@link RestaurantDto}
     */
    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateUpdateRequestDto request
    ) {
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest =
                restaurantMapper.toRestaurantCreateUpdateRequest(request);

        Restaurant restaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest);
        RestaurantDto createdRestaurantDto = restaurantMapper.toRestaurantDto(restaurant);

        return ResponseEntity.ok(createdRestaurantDto);
    }

    /**
     * Searches for restaurants using optional filters like query text, minimum rating,
     * location (latitude/longitude), and search radius.
     *
     * @param q         optional text query
     * @param minRating optional minimum average rating
     * @param latitude  optional latitude for location filtering
     * @param longitude optional longitude for location filtering
     * @param radius    optional radius in kilometers for geo search
     * @param page      page number (1-indexed)
     * @param size      number of results per page
     * @return paginated list of {@link RestaurantSummaryDto}
     */
    @GetMapping
    public Page<RestaurantSummaryDto> searchRestaurants(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Restaurant> searchResult = restaurantService.searchRestaurants(
                q,
                minRating,
                latitude,
                longitude,
                radius,
                PageRequest.of(page - 1, size)
        );
        return searchResult.map(restaurantMapper::toSummaryDto);
    }

    /**
     * Retrieves a single restaurant by its ID.
     *
     * @param restaurantId ID of the restaurant to fetch
     * @return {@link ResponseEntity} with the {@link RestaurantDto}, or 404 if not found
     */
    @GetMapping(path = "/{restaurant_id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable("restaurant_id") String restaurantId){
        return restaurantService.getRestaurant(restaurantId)
                .map(restaurant -> ResponseEntity.ok(restaurantMapper.toRestaurantDto(restaurant)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing restaurant.
     *
     * @param restaurantId ID of the restaurant to update
     * @param requestDto   DTO containing updated restaurant data
     * @return {@link ResponseEntity} with the updated {@link RestaurantDto}
     */
    @PutMapping(path = "/{restaurant_id}")
    public ResponseEntity<RestaurantDto> updateRestaurant(
            @PathVariable("restaurant_id") String restaurantId,
            @Valid @RequestBody RestaurantCreateUpdateRequestDto requestDto
    ){
        RestaurantCreateUpdateRequest request = restaurantMapper
                .toRestaurantCreateUpdateRequest(requestDto);

        Restaurant updatedRestaurant = restaurantService.updateRestaurant(restaurantId, request);

        return ResponseEntity.ok(restaurantMapper.toRestaurantDto(updatedRestaurant));
    }

    /**
     * Deletes a restaurant by its ID.
     *
     * @param restaurantId ID of the restaurant to delete
     * @return {@link ResponseEntity} with 204 No Content
     */
    @DeleteMapping(path = "/{restaurant_id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("restaurant_id") String restaurantId){
        restaurantService.deleteRestaurant(restaurantId);
        return ResponseEntity.noContent().build();
    }
}
