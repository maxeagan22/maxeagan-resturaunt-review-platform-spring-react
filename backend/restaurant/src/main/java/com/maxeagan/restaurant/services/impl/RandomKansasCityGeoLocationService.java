package com.maxeagan.restaurant.services.impl;

import com.maxeagan.restaurant.domain.GeoLocation;
import com.maxeagan.restaurant.domain.entities.Address;
import com.maxeagan.restaurant.services.GeoLocationService;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service implementation that returns a pseudo-random geolocation
 * within the general Kansas City area for a given address.
 */
@Service
public class RandomKansasCityGeoLocationService implements GeoLocationService {

    /**
     * Minimum and maximum latitude value for the Kansas City bounding box.
     */
    private static final float MIN_LATITUDE = 39.00f;
    private static final float MAX_LATITUDE = 39.75f;

    /**
     * Minimum and maximum longitude value for the Kansas City bounding box.
     */
    private static final float MIN_LONGITUDE = -94.75f;
    private static final float MAX_LONGITUDE = -94.45f;

    /**
     * Generates a random latitude and longitude pair within the
     * Kansas City bounding box, regardless of the input address.
     *
     * @param address the address to geolocate (ignored in this implementation)
     * @return a randomly generated {@link GeoLocation} within Kansas City bounds.
     */
    @Override
    public GeoLocation geoLocate(Address address) {
        Random random = new Random();

        // Corrected: Use the LATITUDE bounds for latitude
        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);

        // Corrected: Use the LONGITUDE bounds for longitude
        double longitude = MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
