package dev.kmunton.api.composite.review;

import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;

@Tag(name="Reviews", description="API for getting and adding reviews to restaurants")
public interface ReviewCompositeService {
    @Operation(
            summary =
                    "${api.review.get-reviews-by-restaurant-id.description}",
            description =
                    "${api.review.get-reviews-by-restaurant-id.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description =
                    "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description =
                    "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description =
                    "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
            value    = "/api/v1/reviews/{id}",
            produces = "application/json"
    )
    ReviewsDTO getReviewsByRestaurantId(
            @PathVariable(name="id") Integer id
    );

    @Operation(
            summary =
                    "${api.review.post-review-by-restaurant-id.description}",
            description =
                    "${api.review.post-review-by-restaurant-id.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description =
                    "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description =
                    "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description =
                    "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
            value    = "/api/v1/reviews/{id}",
            consumes = "application/json",
            produces = "application/json"
    )
    ReviewDTO addReviewForRestaurant(
            @PathVariable(name="id") Integer id, @RequestBody @NotNull ReviewRequest request
    );
}
