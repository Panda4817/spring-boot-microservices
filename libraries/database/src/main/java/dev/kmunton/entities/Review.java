package dev.kmunton.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hibernate_lazy_initializer"})
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer rating;
    private String comment;
    private Instant timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;

    public Review() {
    }

    public Review(Integer rating, String comment, Restaurant restaurant) {
        this.rating = rating;
        this.comment = comment;
        this.restaurant = restaurant;
        timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id.equals(review.id) &&
                rating.equals(review.rating) &&
                Objects.equals(comment, review.comment) &&
                restaurant.equals(review.restaurant) &&
                timestamp.equals(review.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, comment, restaurant, timestamp);
    }
}