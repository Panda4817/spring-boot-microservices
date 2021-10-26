package dev.kmunton.api.composite.restaurant;

import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name="Lunchtime Restaurant Search", description="API for searching restaurants given an address and radius distance.")
public interface RestaurantCompositeService {

    @Operation(
            summary =
                    "${api.restaurant-composite.get-restaurants-by-address.description}",
            description =
                    "${api.restaurant-composite.get-restaurants-by-address.notes}")
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
            value    = "/api/v1/restaurants",
            produces = "application/json"
    )
    RestaurantsDTO getRestaurantsByOffice(
            @RequestParam(defaultValue = "1 Crown Ct, London EC2V 6JP, UK")
                    String office,
            @RequestParam(defaultValue = "0.5") String distance
    );

    @Operation(
            summary =
                    "${api.restaurant-composite.post-restaurants.description}",
            description =
                    "${api.restaurant-composite.post-restaurants.notes}")
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
            value    = "/api/v1/restaurants",
            produces = "application/json"
    )
    RestaurantDTO addRestaurant(@RequestBody RestaurantRequest request);


}
