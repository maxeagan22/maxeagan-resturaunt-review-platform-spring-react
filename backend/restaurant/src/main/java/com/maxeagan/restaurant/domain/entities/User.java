package com.maxeagan.restaurant.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Represents a user in the system.
 *
 * This class is designed for use with Elasticsearch and includes mappings for
 * indexing and searching user-related data.
 *
 * Lombok annotations are used to reduce boilerplate:
 * - @Data: generates getters, setters, equals, hashCode, and toString
 * - @Builder: enables fluent object construction
 * - @AllArgsConstructor / @NoArgsConstructor: provide both full and empty constructors
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    /**
     * Unique identifier for the user.
     *
     * Stored as a keyword (not analyzed) for exact-match lookups in Elasticsearch.
     */
    @Field(type = FieldType.Keyword)
    private String id;

    /**
     * User's login or account name.
     *
     * Stored as text and analyzed for full-text search in Elasticsearch.
     */
    @Field(type = FieldType.Text)
    private String username;

    /**
     * User's first name or given name.
     *
     * Stored as text and analyzed for full-text search.
     */
    @Field(type = FieldType.Text)
    private String givenName;

    /**
     * User's last name or family name.
     *
     * Stored as text and analyzed for full-text search.
     */
    @Field(type = FieldType.Text)
    private String familyName;
}
