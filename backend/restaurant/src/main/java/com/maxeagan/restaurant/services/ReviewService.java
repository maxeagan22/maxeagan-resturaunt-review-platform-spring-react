package com.maxeagan.restaurant.services;

import com.maxeagan.restaurant.domain.ReviewCreateUpdateRequest;
import com.maxeagan.restaurant.domain.entities.Review;
import com.maxeagan.restaurant.domain.entities.User;

public interface ReviewService {
    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);
}
