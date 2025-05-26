package com.maxeagan.restaurant.repositories;

import com.maxeagan.restaurant.domain.entities.Restaurant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Elasticsearch repository for performing CRUD operations on Restaurant documents.
 *
 * Extends {@link ElasticsearchRepository} to inherit built-in indexing, search, and delete support.
 * Custom query methods can be added as needed.
 */
@Repository
public interface RestaurantRepository extends ElasticsearchRepository<Restaurant, String> {
    // TODO: Add custom queries (e.g., search by name, cuisine, or location)
}
