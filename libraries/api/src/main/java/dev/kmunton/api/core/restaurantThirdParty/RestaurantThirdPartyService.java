package dev.kmunton.api.core.restaurantThirdParty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface RestaurantThirdPartyService {

    @GetMapping(
            value    = "/api/v1/thirdparty/restaurants",
            produces = "application/json"
    )
    RestaurantResponse getRestaurantsPage(@RequestParam String offset, @RequestParam String lat, @RequestParam String lng);

}