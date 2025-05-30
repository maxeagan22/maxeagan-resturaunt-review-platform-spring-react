package com.maxeagan.restaurant.services;

import com.maxeagan.restaurant.domain.GeoLocation;
import com.maxeagan.restaurant.domain.entities.Address;

/**
 * Service interface for determining the geographic coordinates (latitude and longitude)
 * of a given address.
 * <p>
 * Implementations of this interface may use external geocoding APIs (e.g., Google Maps, Mapbox)
 * or internal logic (e.g., random coordinate generation within a bounding box for testing purposes).
 */
public interface GeoLocationService {

    /**
     * Computes or retrieves the {@link GeoLocation} (latitude and longitude) for the given {@link Address}.
     *
     * @param address the address to geolocate
     * @return the geographic coordinates corresponding to the provided address
     */
    GeoLocation geoLocate(Address address);
}
