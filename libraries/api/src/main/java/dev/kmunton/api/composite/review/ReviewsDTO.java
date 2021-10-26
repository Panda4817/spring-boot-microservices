package dev.kmunton.api.composite.review;

import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.review.ReviewDTO;

import java.util.List;

public class ReviewsDTO {

    private List<ReviewDTO> data;
    private RestaurantDTO restaurant;

    public ReviewsDTO() {
    }

    public ReviewsDTO(List<ReviewDTO> data, RestaurantDTO restaurant) {
        this.data = data;
        this.restaurant = restaurant;
    }

    public List<ReviewDTO> getData() {
        return data;
    }

    public void setData(List<ReviewDTO> data) {
        this.data = data;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }
}
