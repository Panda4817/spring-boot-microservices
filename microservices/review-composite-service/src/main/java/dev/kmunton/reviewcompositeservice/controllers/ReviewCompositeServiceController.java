package dev.kmunton.reviewcompositeservice.controllers;

import dev.kmunton.api.composite.review.ReviewCompositeService;
import dev.kmunton.api.composite.review.ReviewsDTO;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import dev.kmunton.api.exceptions.InvalidInputException;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.reviewcompositeservice.services.ReviewCompositeServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class ReviewCompositeServiceController implements ReviewCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewCompositeServiceController.class);


    @Autowired
    private ReviewCompositeServiceImplementation revService;

    @Override
    public ReviewsDTO getReviewsByRestaurantId(Integer id) {
        LOG.debug("/reviews/{} return reviews for id provided", id);
        if (id < 1) {
            throw new InvalidInputException("Invalid restaurant id: " + id);
        }
        try {
            RestaurantDTO restaurantDTO = revService.getRestaurantFromId(id);
            List<ReviewDTO> reviews = revService.getReviews(id);
            return new ReviewsDTO(reviews, restaurantDTO);
        } catch (NotFoundException ex) {
            throw new NotFoundException("No restaurant found with id: " + id);
        }


    }

    @Override
    public ReviewDTO addReviewForRestaurant(Integer id, @NotNull ReviewRequest request) {
        LOG.debug("/reviews/{} add a review for restaurant with id provided", id);
        if (id < 1) {
            throw new InvalidInputException("Invalid restaurant id: " + id);
        }
        try {
            RestaurantDTO restaurantDTO = revService.getRestaurantFromId(id);
            request.setRestaurant(restaurantDTO);
            return revService.addReview(request);
        } catch (NotFoundException ex) {
            throw new NotFoundException("No restaurant found with id: " + id);
        }


    }
}
