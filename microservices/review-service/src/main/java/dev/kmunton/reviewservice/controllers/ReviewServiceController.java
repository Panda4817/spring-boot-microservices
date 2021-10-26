package dev.kmunton.reviewservice.controllers;

import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import dev.kmunton.api.core.review.ReviewService;
import dev.kmunton.api.exceptions.InvalidInputException;
import dev.kmunton.entities.Restaurant;
import dev.kmunton.entities.Review;
import dev.kmunton.reviewservice.services.ReviewServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class ReviewServiceController implements ReviewService {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceController.class);

    @Autowired
    private ReviewServiceImplementation dbService;

    @Override
    public List<ReviewDTO> getReviewsByRestaurantId(Integer id) {
        LOG.debug("/reviews/{} return list of reviews for a restaurant given restaurant id", id);
        if (id < 1) {
            throw new InvalidInputException("Invalid restaurant id: " + id);
        }

//        Restaurant res = resService.getRestaurantById(id);
//        if (res.getId() == null) {
//            throw new NotFoundException("No restaurant found with id: " + id);
//        }
        List<Review> revs = dbService.getReviewsByRestaurantId(id);

//        RestaurantDTO restaurant = resService.prepareOneRestaurantObject(res);
        List<ReviewDTO> reviews = dbService.prepareReviewList(revs);

//        return new ReviewsDTO(reviews, restaurant);
        return reviews;
    }

    @Override
    public ReviewDTO addReviewForRestaurant(@NotNull ReviewRequest request) {
        LOG.debug("/reviews/{} add a review for a restaurant given restaurant id", request.getRestaurant().getId());
        if (request.getRestaurantId() < 1) {
            throw new InvalidInputException("Invalid restaurant id: " + request.getRestaurantId());
        }

        Restaurant restaurant = new Restaurant(
                request.getRestaurant().getName(),
                request.getRestaurant().getDescription(),
                request.getRestaurant().getWebsite(),
                request.getRestaurant().getAddress()
        );
        restaurant.setId(request.getRestaurantId());
        Review review = dbService.addReviewForRestaurant(restaurant, request);
        return new ReviewDTO(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getTimestamp()
        );
    }
}
