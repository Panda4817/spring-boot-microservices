package dev.kmunton.api.core.review;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ReviewService {


    @GetMapping(
            value    = "/api/v1/database/reviews/{id}",
            produces = "application/json"
    )
    List<ReviewDTO> getReviewsByRestaurantId(
            @PathVariable(name="id") Integer id
    );


    @PostMapping(
            value    = "/api/v1/database/reviews",
            consumes = "application/json",
            produces = "application/json"
    )
    ReviewDTO addReviewForRestaurant(
            @RequestBody @NotNull ReviewRequest request
    );
}
