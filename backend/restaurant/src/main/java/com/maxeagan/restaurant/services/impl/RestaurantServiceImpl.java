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
}
