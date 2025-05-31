package com.maxeagan.restaurant.domain.dtos;

import com.maxeagan.restaurant.domain.entities.Address;
import com.maxeagan.restaurant.domain.entities.OperatingHours;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating or updating a Restaurant entity.
 * <p>
 * Contains all the required input fields for a new restaurant,
 * including validation constraints for client input.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantCreateUpdateRequestDto {

    /**
     * The name of the restaurant.
     * Must not be blank.
     */
    @NotBlank(message = "Restaurant name is required")
    private String name;

    /**
     * The cuisine type offered by the restaurant (e.g., Italian, Mexican).
     * Must not be blank.
     */
    @NotBlank(message = "Cuisine type is require")
    private String cuisineType;

    /**
     * Contact information for the restaurant, such as a phone number or email.
     * Must not be blank.
     */
    @NotBlank(message = "Contact information is required")
    private String contactInformation;

    /**
     * The restaurant's address details.
     * Must pass validation of the nested {@link AddressDto}.
     */
    @Valid
    private AddressDto address;

    /**
     * The restaurant's weekly operating hours.
     * Validates the structure of {@link OperatingHours}.
     */
    @Valid
    private OperatingHoursDto operatingHours;

    /**
     * List of photo IDs (UUIDs or database identifiers) associated with the restaurant.
     * Must contain at least one ID.
     */
    @Size(min = 1, message = "At least one photo ID is required")
    private List<String> photoIds;
}
