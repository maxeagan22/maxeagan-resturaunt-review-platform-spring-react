package com.maxeagan.restaurant.services;

import com.maxeagan.restaurant.domain.ReviewCreateUpdateRequest;
import com.maxeagan.restaurant.domain.entities.Review;
import com.maxeagan.restaurant.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service interface for handling review-related business logic.
 * Supports creation, retrieval, update, deletion, and listing of reviews.
 */
public interface ReviewService {

    /**
     * Creates a new review for a given restaurant by the specified user.
     *
     * @param author       the user creating the review
     * @param restaurantId the ID of the restaurant being reviewed
     * @param review       review input data
     * @return the created {@link Review}
     */
    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);

    /**
     * Lists all reviews for a specific restaurant, paginated.
     *
     * @param restaurantId the restaurant to list reviews for
     * @param pageable     pagination and sorting info
     * @return paginated list of {@link Review} objects
     */
    Page<Review> listReviews(String restaurantId, Pageable pageable);

    /**
     * Retrieves a single review by restaurant and review ID.
     *
     * @param restaurantId the restaurant ID
     * @param reviewId     the review ID
     * @return an {@link Optional} containing the review if found
     */
    Optional<Review> getReview(String restaurantId, String reviewId);

    /**
     * Updates an existing review written by the given user.
     *
     * @param author       the user attempting the update
     * @param restaurantId the restaurant ID
     * @param reviewId     the ID of the review to update
     * @param review       new review data
     * @return the updated {@link Review}
     */
    Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest review);

    /**
     * Deletes a review by ID for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @param reviewId     the ID of the review to delete
     */
    void deleteReview(String restaurantId, String reviewId);
}
