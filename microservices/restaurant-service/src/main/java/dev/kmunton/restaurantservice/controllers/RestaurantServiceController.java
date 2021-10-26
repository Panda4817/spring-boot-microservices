package dev.kmunton.restaurantservice.controllers;

import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import dev.kmunton.api.core.restaurant.RestaurantService;
import dev.kmunton.api.exceptions.InvalidInputException;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.entities.Restaurant;
import dev.kmunton.entities.RestaurantToSite;
import dev.kmunton.restaurantservice.services.RestaurantServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantServiceController implements RestaurantService {
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantServiceController.class);

    @Autowired
    private RestaurantServiceImplementation dbService;

    @Override
    public List<RestaurantDTO> getRestaurantsBySiteId(Integer id, String distance) {
        LOG.debug("/restaurants/{} return restaurants for siteId and distance={}", id, distance);
        Double chosenDistance = Double.valueOf(distance);
        if (chosenDistance <= 0.0 || chosenDistance > 0.5) {
            throw new InvalidInputException("Invalid distance: " + distance);
        }
        if (id < 1) {
            throw new InvalidInputException("Invalid site id: " + id);
        }
        List<RestaurantToSite> restaurants = dbService.getRestaurantsBySiteId(id, chosenDistance);
        List<RestaurantDTO> restaurantsResponse = dbService.prepareRestaurantList(restaurants);

        return restaurantsResponse;
    }

    @Override
    public RestaurantDTO getRestaurantById(Integer id) {
        LOG.debug("/restaurant/{} return restaurant by id", id);
        if (id < 1) {
            throw new InvalidInputException("Invalid restaurant id: " + id);
        }
        Restaurant res = dbService.getRestaurantById(id);
        if (res.getId() == null) {
            throw new NotFoundException("No restaurant found with id: " + id);
        }
        return dbService.prepareOneRestaurantObject(res);
    }

    @Override
    public RestaurantDTO addRestaurant(RestaurantRequest request) {
        LOG.debug("/restaurants add a restaurant to database and return new restaurant with id");
        Restaurant res = dbService.saveRestaurant(request, request.getSite());
        RestaurantDTO restaurant  = dbService.prepareOneRestaurantObject(res);
        restaurant.setDistance(request.getDistance());
        return restaurant;
    }
}
