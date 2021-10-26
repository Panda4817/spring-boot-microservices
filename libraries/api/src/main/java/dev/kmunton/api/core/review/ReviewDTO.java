package dev.kmunton.api.core.review;

import java.time.Instant;
import java.util.Objects;

public class ReviewDTO {
    private Integer id;
    private Integer rating;
    private String comment;
    private Instant timestamp;

    public ReviewDTO() {
    }

    public ReviewDTO(Integer id, Integer rating, String comment, Instant timestamp) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        if (timestamp != null) {
            this.timestamp = timestamp;
        } else {
            this.timestamp = Instant.now();
        }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDTO reviewDTO = (ReviewDTO) o;
        return id.equals(reviewDTO.id) &&
                rating.equals(reviewDTO.rating) &&
                Objects.equals(comment, reviewDTO.comment) &&
                timestamp.equals(reviewDTO.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rating, comment, timestamp);
    }
}
