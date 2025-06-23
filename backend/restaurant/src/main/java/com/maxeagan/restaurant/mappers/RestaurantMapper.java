package com.maxeagan.restaurant.mappers;

import com.maxeagan.restaurant.domain.RestaurantCreateUpdateRequest;
import com.maxeagan.restaurant.domain.dtos.GeoPointDto;
import com.maxeagan.restaurant.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.maxeagan.restaurant.domain.dtos.RestaurantDto;
import com.maxeagan.restaurant.domain.dtos.RestaurantSummaryDto;
import com.maxeagan.restaurant.domain.entities.Restaurant;
import com.maxeagan.restaurant.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

/**
 * Mapper interface for converting between Restaurant-related domain objects and DTOs.
 * Uses MapStruct for automatic implementation generation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    /**
     * Converts a {@link RestaurantCreateUpdateRequestDto} to a {@link RestaurantCreateUpdateRequest}.
     *
     * @param dto The DTO to convert.
     * @return A domain model suitable for service or persistence logic.
     */
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateRequestDto dto);

    /**
     * Converts a {@link Restaurant} entity to a {@link RestaurantDto}, including review count.
     *
     * @param restaurant The Restaurant entity.
     * @return A detailed Restaurant DTO including total reviews.
     */
    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantDto toRestaurantDto(Restaurant restaurant);

    /**
     * Converts a {@link Restaurant} entity to a {@link RestaurantSummaryDto}, including review count.
     *
     * @param restaurant The Restaurant entity.
     * @return A summary DTO with total reviews and limited fields.
     */
    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantSummaryDto toSummaryDto(Restaurant restaurant);

    /**
     * Helper method used by MapStruct to calculate the total number of reviews for a restaurant.
     *
     * @param reviews List of {@link Review} objects.
     * @return The number of reviews, or 0 if the list is null.
     */
    @Named("populateTotalReviews")
    default Integer populateTotalReviews(List<Review> reviews) {
        return (reviews != null) ? reviews.size() : 0;
    }

    /**
     * Converts an Elasticsearch {@link GeoPoint} to a {@link GeoPointDto}.
     *
     * @param geoPoint The GeoPoint from Elasticsearch.
     * @return A DTO containing latitude and longitude.
     */
    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);
}
