package com.maxeagan.restaurant.controllers;

import com.maxeagan.restaurant.domain.RestaurantCreateUpdateRequest;
import com.maxeagan.restaurant.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.maxeagan.restaurant.domain.dtos.RestaurantDto;
import com.maxeagan.restaurant.domain.entities.Restaurant;
import com.maxeagan.restaurant.mappers.RestaurantMapper;
import com.maxeagan.restaurant.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing restaurant creation operations.
 * <p>
 * Handles HTTP POST requests to create new restaurant records in the system.
 */
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    /**
     * Creates a new restaurant based on the provided input data.
     * <p>
     * Validates the request body, maps the DTO to the internal domain model,
     * delegates the creation logic to the service layer, and returns the created
     * restaurant as a DTO in the response.
     *
     * @param request the validated input data for creating a restaurant
     * @return ResponseEntity containing the created RestaurantDto
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
}
