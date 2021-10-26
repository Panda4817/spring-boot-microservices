package dev.kmunton.api.core.review;

import dev.kmunton.api.core.restaurant.RestaurantDTO;

import java.util.Objects;

public class ReviewRequest {
    private Integer rating;
    private String comment;
    private Integer restaurantId;
    private RestaurantDTO restaurant;

    public ReviewRequest() {
    }

    public ReviewRequest(Integer rating, String comment, Integer restaurantId) {
        this.rating = rating;
        this.comment = comment;
        this.restaurantId = restaurantId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewRequest that = (ReviewRequest) o;
        return rating.equals(that.rating) &&
                Objects.equals(comment, that.comment) &&
                restaurantId.equals(that.restaurantId) &&
                Objects.equals(restaurant, that.restaurant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, comment, restaurantId, restaurant);
    }
}