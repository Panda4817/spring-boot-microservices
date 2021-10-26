package dev.kmunton.reviewservice.repositories;

import dev.kmunton.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT rv FROM Review rv INNER JOIN rv.restaurant r WHERE r.id = :id ORDER BY rv.timestamp DESC")
    List<Review> findByRestaurantId(@Param("id") Integer id);
}
