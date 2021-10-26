package dev.kmunton.restaurantthirdpartyservice.controllers;

import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantThirdPartyService;
import dev.kmunton.restaurantthirdpartyservice.services.RestaurantThirdPartyServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantThirdPartyServiceController implements RestaurantThirdPartyService {

    private static final Logger LOG = LoggerFactory.getLogger(RestaurantThirdPartyServiceController.class);

    @Autowired
    private RestaurantThirdPartyServiceImplementation resService;

    @Override
    public RestaurantResponse getRestaurantsPage(String offset, String lat, String lng) {
        LOG.debug("/restaurants?offset={}&lat={}&lng={} returns list of restaurants around a particular site", offset, lat, lng);
        RestaurantResponse restaurants =  resService.getRestaurantsPerPage(offset, lat, lng);
        return restaurants;
    }
}
