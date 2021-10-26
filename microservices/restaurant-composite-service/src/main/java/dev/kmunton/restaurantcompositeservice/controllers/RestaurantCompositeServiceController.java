package dev.kmunton.restaurantcompositeservice.controllers;

import dev.kmunton.api.composite.restaurant.RestaurantCompositeService;
import dev.kmunton.api.composite.restaurant.RestaurantsDTO;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.api.exceptions.InvalidInputException;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.restaurantcompositeservice.services.RestaurantCompositeServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantCompositeServiceController implements RestaurantCompositeService {
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantCompositeServiceController.class);

    @Autowired
    private RestaurantCompositeServiceImplementation resService;


    @Override
    public RestaurantsDTO getRestaurantsByOffice(String office, String distance) {
        LOG.debug("/restaurants?office={}&distance={} return restaurants", office, distance);
        Double chosenDistance;
        try{
            chosenDistance = Double.valueOf(distance);
        } catch(NumberFormatException ex) {
            throw new InvalidInputException("Distance is not a number");
        }

        if (chosenDistance <= 0.0 || chosenDistance > 0.5) {
            throw new InvalidInputException("Invalid distance: " + distance);
        }
        RestaurantsDTO response = resService.getRestaurantsByOffice(office, chosenDistance);
        if (response.getSite().getId() == null) {
            throw new NotFoundException("Site could not be found from office address");
        }
        return response;


    }

    @Override
    public RestaurantDTO addRestaurant(RestaurantRequest request) {
        LOG.debug("/restaurants returns added restaurant");
        if (request.getSite().getId() < 1) {
            throw new InvalidInputException("Invalid site id: " + request.getSite().getId());
        }
        SiteDTO site = resService.getSite(request.getSite().getId());
        if (site.getId() == null) {
            throw new NotFoundException("No site found with id: " + request.getSite().getId());
        }
        RestaurantDTO response = resService.addRestaurant(request);
        return response;
    }
}
