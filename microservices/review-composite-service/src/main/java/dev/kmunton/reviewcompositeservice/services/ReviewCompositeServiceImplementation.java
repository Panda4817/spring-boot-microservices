package dev.kmunton.reviewcompositeservice.services;

import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import dev.kmunton.reviewcompositeservice.integration.ReviewCompositeIntegration;
import dev.kmunton.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewCompositeServiceImplementation {
    private ServiceUtil serviceUtil;
    private ReviewCompositeIntegration integration;

    @Autowired
    public ReviewCompositeServiceImplementation(ServiceUtil serviceUtil, ReviewCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    public RestaurantDTO getRestaurantFromId(Integer id) {
        RestaurantDTO res = integration.getRestaurantById(id);
        return res;
    }

    public List<ReviewDTO> getReviews(Integer id) {
        return integration.getReviewsByRestaurantId(id);
    }

    public ReviewDTO addReview(ReviewRequest req) {
        return integration.addReviewForRestaurant(req);
    }


}
