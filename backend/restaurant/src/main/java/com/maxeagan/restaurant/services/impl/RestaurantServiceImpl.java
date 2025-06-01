package com.maxeagan.restaurant.services.impl;

import com.maxeagan.restaurant.domain.GeoLocation;
import com.maxeagan.restaurant.domain.RestaurantCreateUpdateRequest;
import com.maxeagan.restaurant.domain.entities.Address;
import com.maxeagan.restaurant.domain.entities.Photo;
import com.maxeagan.restaurant.domain.entities.Restaurant;
import com.maxeagan.restaurant.repositories.RestaurantRepository;
import com.maxeagan.restaurant.services.GeoLocationService;
import com.maxeagan.restaurant.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing restaurant entities.
 * <p>
 * Responsible for processing restaurant creation requests,
 * generating geolocation data, and persisting the resulting entity.
 */
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    /**
     * Creates and saves a new {@link Restaurant} entity based on the incoming request.
     * <p>
     * Steps:
     * <ul>
     *   <li>Resolves geolocation based on the address using {@link GeoLocationService}.</li>
     *   <li>Converts photo URLs into {@link Photo} entities with timestamps.</li>
     *   <li>Builds and saves a {@link Restaurant} entity via the repository.</li>
     * </ul>
     *
     * @param request the DTO containing all necessary data to create a new restaurant
     * @return the saved {@link Restaurant} entity
     */
    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request) {
        Address address = request.getAddress();
        GeoLocation geoLocation = geoLocationService.geoLocate(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos = photoIds.stream()
                .map(photoUrl -> Photo.builder()
                        .url(photoUrl)
                        .uploadDate(LocalDateTime.now())
                        .build())
                .toList();

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .contactInformation(request.getContactInformation())
                .address(address)
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();

        return restaurantRepository.save(restaurant);
    }

    /**
     * Implements restaurant search logic based on multiple optional criteria:
     * text query, minimum rating, and geographic proximity.
     *
     * <p>The method prioritizes filters in the following order:
     * <ol>
     *   <li>If {@code minRating} is provided but {@code query} is null or empty,
     *       it returns restaurants with an average rating >= {@code minRating}.</li>
     *   <li>If {@code query} is non-empty (regardless of whether {@code minRating} is set),
     *       it performs a fuzzy search on name and cuisine type, filtered by {@code minRating}
     *       (or 0 if null).</li>
     *   <li>If geographic coordinates and a radius are provided (non-null),
     *       it returns restaurants within the specified radius (in miles) of the location.</li>
     *   <li>If no filters are provided, it returns all restaurants paginated.</li>
     * </ol>
     *
     * @param query      optional search term to match against name or cuisine type (fuzzy)
     * @param minRating  optional minimum average rating filter (inclusive)
     * @param latitude   optional latitude for geo-distance filtering
     * @param longitude  optional longitude for geo-distance filtering
     * @param radius     optional radius (in miles) for geo-distance filtering
     * @param pageable   pagination and sorting information
     * @return a {@link Page} of {@link Restaurant} matching the applied criteria
     */
    @Override
    public Page<Restaurant> searchRestaurants(
            String query, Float minRating, Float latitude,
            Float longitude, Float radius, Pageable pageable) {

        if (null != minRating && (null == query || query.isEmpty())) {
            return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        Float searchMinRating = null == minRating ? 0f : minRating;

        if (null != query && !query.trim().isEmpty()) {
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        if (null != latitude && null != longitude && null != radius) {
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        return restaurantRepository.findAll(pageable);
    }

}
