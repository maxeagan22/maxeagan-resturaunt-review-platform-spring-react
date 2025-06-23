package com.maxeagan.restaurant.mappers;

import com.maxeagan.restaurant.domain.ReviewCreateUpdateRequest;
import com.maxeagan.restaurant.domain.dtos.ReviewCreateUpdateRequestDto;
import com.maxeagan.restaurant.domain.dtos.ReviewDto;
import com.maxeagan.restaurant.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between Review-related DTOs and domain models.
 * Uses MapStruct to generate the implementation at compile time.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    /**
     * Converts a {@link ReviewCreateUpdateRequestDto} to a domain-level {@link ReviewCreateUpdateRequest}.
     *
     * @param dto the incoming DTO with user-submitted review data
     * @return mapped {@link ReviewCreateUpdateRequest} for service/business logic
     */
    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto dto);

    /**
     * Converts a {@link Review} entity to a {@link ReviewDto} for client-facing response.
     *
     * @param review the review entity from the database
     * @return a DTO suitable for JSON response
     */
    ReviewDto toDto(Review review);
}
