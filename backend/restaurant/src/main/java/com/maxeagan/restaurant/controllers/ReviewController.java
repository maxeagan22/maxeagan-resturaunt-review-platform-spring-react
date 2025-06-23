package com.maxeagan.restaurant.controllers;

import com.maxeagan.restaurant.domain.ReviewCreateUpdateRequest;
import com.maxeagan.restaurant.domain.dtos.ReviewCreateUpdateRequestDto;
import com.maxeagan.restaurant.domain.dtos.ReviewDto;
import com.maxeagan.restaurant.domain.entities.Review;
import com.maxeagan.restaurant.domain.entities.User;
import com.maxeagan.restaurant.mappers.ReviewMapper;
import com.maxeagan.restaurant.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing reviews tied to specific restaurants.
 */
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    /**
     * Creates a new review for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant being reviewed
     * @param review       DTO with review input data
     * @param jwt          the authenticated user's JWT
     * @return created {@link ReviewDto}
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt) {

        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.toReviewCreateUpdateRequest(review);
        User user = jwtToUser(jwt);
        Review createdReview = reviewService.createReview(user, restaurantId, reviewCreateUpdateRequest);

        return ResponseEntity.ok(reviewMapper.toDto(createdReview));
    }

    /**
     * Retrieves a paginated list of reviews for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @param pageable     pagination and sorting settings
     * @return paged list of {@link ReviewDto}
     */
    @GetMapping
    public Page<ReviewDto> listReviews(
            @PathVariable String restaurantId,
            @PageableDefault(
                    size = 20,
                    page = 0,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return reviewService
                .listReviews(restaurantId, pageable)
                .map(reviewMapper::toDto);
    }

    /**
     * Retrieves a single review by its ID for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @param reviewId     the ID of the review
     * @return {@link ReviewDto} if found, otherwise 204 No Content
     */
    @GetMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId
    ) {
        return reviewService.getReview(restaurantId, reviewId)
                .map(reviewMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /**
     * Updates a review written by the authenticated user.
     *
     * @param restaurantId the restaurant ID
     * @param reviewId     the review ID
     * @param review       updated review data
     * @param jwt          the authenticated user's JWT
     * @return updated {@link ReviewDto}
     */
    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt
    ) {
        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.toReviewCreateUpdateRequest(review);
        User user = jwtToUser(jwt);

        Review updatedReview = reviewService.updateReview(
                user, restaurantId, reviewId, reviewCreateUpdateRequest
        );

        return ResponseEntity.ok(reviewMapper.toDto(updatedReview));
    }

    /**
     * Deletes a review by its ID for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @param reviewId     the review ID
     * @return 204 No Content
     */
    @DeleteMapping(path = "/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId
    ) {
        reviewService.deleteReview(restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Converts a JWT token into a {@link User} entity.
     *
     * @param jwt JWT token from Spring Security
     * @return a {@link User} instance with user identity
     */
    private User jwtToUser(Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }
}
