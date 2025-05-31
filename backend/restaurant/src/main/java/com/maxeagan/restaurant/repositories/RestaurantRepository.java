package com.maxeagan.restaurant.repositories;

import com.maxeagan.restaurant.domain.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Elasticsearch repository for performing CRUD operations on Restaurant documents.
 *
 * Extends {@link ElasticsearchRepository} to inherit built-in indexing, search, and delete support.
 * Includes custom queries for advanced search functionality.
 */
@Repository
public interface RestaurantRepository extends ElasticsearchRepository<Restaurant, String> {

    /**
     * Finds restaurants with an average rating greater than or equal to the specified minimum rating.
     *
     * @param minRating minimum average rating to filter by
     * @param pageable  pagination and sorting information
     * @return a paginated list of restaurants matching the rating filter
     */
    Page<Restaurant> findByAverageRatingGreaterThanEqual(Float minRating, Pageable pageable);

    /**
     * Performs a fuzzy text search across restaurant names and cuisine types,
     * while also filtering results by a minimum average rating.
     *
     * <p>The query logic:
     * <ul>
     *   <li>Must have an averageRating >= minRating</li>
     *   <li>Should match the query string fuzzily against the name or cuisineType</li>
     *   <li>At least one "should" clause must match (minimum_should_match = 1)</li>
     * </ul>
     *
     * @param query     the text input to search for (fuzzy matched)
     * @param minRating minimum average rating to filter by
     * @param pageable  pagination and sorting information
     * @return a paginated list of restaurants matching the criteria
     */
    @Query("{" +
            " \"bool\": {" +
            " \"must\": [" +
            " {\"range\": {\"averageRating\": {\"gte\": ?1}}}" +
            " ]," +
            " \"should\": [" +
            " {\"fuzzy\": {\"name\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}}," +
            " {\"fuzzy\": {\"cuisineType\": {\"value\": \"?0\", \"fuzziness\": \"AUTO\"}}}" +
            " ]," +
            " \"minimum_should_match\": 1" +
            " }" +
            "}")
    Page<Restaurant> findByQueryAndMinRating(String query, Float minRating, Pageable pageable);

    /**
     * Finds restaurants within a specified radius (in miles) from a given geographic point.
     *
     * <p>This uses a geo-distance filter based on latitude and longitude.
     *
     * @param latitude  the central point's latitude
     * @param longitude the central point's longitude
     * @param radiusMi  the radius to search within (in miles)
     * @param pageable  pagination and sorting information
     * @return a paginated list of nearby restaurants
     */
    @Query("{" +
            " \"bool\": {" +
            " \"must\": [" +
            " {\"geo_distance\": {" +
            " \"distance\": \"?2mi\"," +
            " \"geoLocation\": {" +
            " \"lat\": ?0," +
            " \"lon\": ?1" +
            " }" +
            " }}" +
            " ]" +
            " }" +
            "}")
    Page<Restaurant> findByLocationNear(
            Float latitude,
            Float longitude,
            Float radiusMi,
            Pageable pageable);

}
