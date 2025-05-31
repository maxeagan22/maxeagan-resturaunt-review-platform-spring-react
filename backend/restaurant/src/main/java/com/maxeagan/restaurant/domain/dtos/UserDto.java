package com.maxeagan.restaurant.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing a user in the system.
 * <p>
 * Contains basic identifying information used in contexts such as reviews,
 * restaurant ownership, or administrative actions.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    /**
     * Unique identifier for the user.
     */
    private String id;

    /**
     * Username chosen by the user (typically unique).
     */
    private String username;

    /**
     * User's first or given name.
     */
    private String givenName;

    /**
     * User's last or family name.
     */
    private String familyName;
}
