package dev.kmunton.reviewservice.services;

import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import dev.kmunton.entities.Restaurant;
import dev.kmunton.entities.Review;
import dev.kmunton.reviewservice.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImplementation {
    @Autowired
    private ReviewRepository reviewRepo;

    public List<Review> getReviewsByRestaurantId(Integer id) {
        return reviewRepo.findByRestaurantId(id);
    }

    public Review addReviewForRestaurant(Restaurant restaurant, ReviewRequest request) {
        Review review = new Review(request.getRating(), request.getComment(), restaurant);
        review = reviewRepo.save(review);
        restaurant.addReview(review);
        return review;
    }

    public List<ReviewDTO> prepareReviewList(List<Review> reviews) {
        List<ReviewDTO> reviewsResponse = new ArrayList<>();
        for (Review r: reviews) {
            ReviewDTO rev = new ReviewDTO(
                    r.getId(),
                    r.getRating(),
                    r.getComment(),
                    r.getTimestamp()
            );
            reviewsResponse.add(rev);
        }
        return reviewsResponse;
    }

}
