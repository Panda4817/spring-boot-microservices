package dev.kmunton.api.core.restaurant;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RestaurantService {

    @GetMapping(
            value    = "/api/v1/database/restaurants/{id}",
            produces = "application/json"
    )
    List<RestaurantDTO> getRestaurantsBySiteId(
            @PathVariable(name="id") Integer id,
            @RequestParam(defaultValue = "0.5") String distance
    );

    @GetMapping(
            value    = "/api/v1/database/restaurant/{id}",
            produces = "application/json"
    )
    RestaurantDTO getRestaurantById(@PathVariable(name="id") Integer id);

    @PostMapping(
            value    = "/api/v1/database/restaurants",
            produces = "application/json"
    )
    RestaurantDTO addRestaurant(@RequestBody RestaurantRequest request);
}
