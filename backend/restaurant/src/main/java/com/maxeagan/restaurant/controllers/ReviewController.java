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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService; 

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt){
        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.toReviewCreateUpdateRequest(review);

        User user = jwtToUser(jwt);

        Review createdReview =  reviewService.createReview(user, restaurantId, reviewCreateUpdateRequest);

        return ResponseEntity.ok(reviewMapper.toDto(createdReview));
    }

    private User jwtToUser(Jwt jwt){
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }


}
