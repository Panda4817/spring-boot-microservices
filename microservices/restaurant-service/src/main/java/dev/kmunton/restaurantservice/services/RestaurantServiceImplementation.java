package dev.kmunton.restaurantservice.services;

import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.entities.Restaurant;
import dev.kmunton.entities.RestaurantToSite;
import dev.kmunton.entities.Site;
import dev.kmunton.restaurantservice.repositories.RestaurantRepository;
import dev.kmunton.restaurantservice.repositories.RestaurantToSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantServiceImplementation {

    @Autowired
    private RestaurantRepository restRepo;

    @Autowired
    private RestaurantToSiteRepository restToSiteRepo;


    public List<RestaurantToSite> getRestaurantsBySiteId(Integer id, Double distance) {
        return restToSiteRepo.findAllBySite(id, distance);
    }

    public Restaurant saveRestaurant(RestaurantRequest res, SiteDTO site) {
        Restaurant resEntity = new Restaurant(
                res.getName(),
                res.getDescription(),
                res.getWebsite(),
                res.getAddress()
        );
        Restaurant savedRes = restRepo.save(resEntity);
        Site savedSite = new Site(site.getName(), site.getLat(), site.getLng(), new ArrayList<>());
        savedSite.setId(site.getId());
        RestaurantToSite resToSite = savedRes.addSite(savedSite, res.getDistance());
        RestaurantToSite savedRestToSite = restToSiteRepo.save(resToSite);
        return savedRes;
    }

    public Restaurant getRestaurantById(Integer id) {
        List<Restaurant> restaurants = restRepo.findAllById(id);
        if (restaurants.isEmpty()) {
            return new Restaurant();
        }
        Restaurant restaurant = restaurants.get(0);
        return restaurant;

    }

    public List<RestaurantDTO> prepareRestaurantList(List<RestaurantToSite> restaurants) {
        List<RestaurantDTO> restaurantsResponse = new ArrayList<>();
        for (RestaurantToSite r : restaurants){
            Restaurant res = r.getRestaurant();
            RestaurantDTO resResp = new RestaurantDTO(
                    res.getId(),
                    res.getName(),
                    res.getDescription(),
                    res.getWebsite(),
                    res.getAddress(),
                    r.getDistance()
            );
            restaurantsResponse.add(resResp);
        }
        return restaurantsResponse;
    }

    public RestaurantDTO prepareOneRestaurantObject(Restaurant res) {
        return new RestaurantDTO(
                res.getId(),
                res.getName(),
                res.getDescription(),
                res.getWebsite(),
                res.getAddress(),
                0.0
        );
    }


}
