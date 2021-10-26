package dev.kmunton.reviewservice.repositories;


import dev.kmunton.entities.Restaurant;
import dev.kmunton.entities.Review;
import dev.kmunton.testdatabase.PostgreSqlTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/data.sql"})
class ReviewRepositoryTest extends PostgreSqlTestBase {
    @Autowired
    private ReviewRepository reviewRepo;

    @Test
    void saveAndFindByRestaurantId(){
        Restaurant resEntity = new Restaurant(
                "name",
                "",
                "",
                "address"
        );
        resEntity.setId(1);
        List<Review> reviews = reviewRepo.findByRestaurantId(resEntity.getId());
        assertEquals(true, reviews.isEmpty());

        Review review = new Review(
                5,
                "comment",
                resEntity
        );
        Review savedReview = reviewRepo.save(review);
        reviews = reviewRepo.findByRestaurantId(resEntity.getId());
        assertEquals(false, reviews.isEmpty());

    }

}