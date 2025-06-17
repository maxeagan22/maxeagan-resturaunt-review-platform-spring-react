package com.maxeagan.restaurant.exceptions;


/**
 * Exception thrown when a restaurant entity cannot be found.
 *
 * <p>This exception is typically used in service or repository layers to indicate that a
 * restaurant with the specified criteria (e.g., ID or name) does not exist in the system.</p>
 *
 * <p>It extends {@link BaseException}, allowing for consistent exception handling and
 * structured error response construction.</p>
 */
public class RestaurantNotFoundException extends  BaseException {
    public RestaurantNotFoundException() {
    }

    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(Throwable cause) {
        super(cause);
    }

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
