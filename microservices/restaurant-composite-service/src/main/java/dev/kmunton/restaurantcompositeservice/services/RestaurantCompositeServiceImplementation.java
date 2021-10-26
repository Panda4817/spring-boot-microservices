package dev.kmunton.restaurantcompositeservice.services;

import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.api.composite.restaurant.RestaurantsDTO;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantJson;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.api.core.site.SiteRequest;
import dev.kmunton.restaurantcompositeservice.integration.RestaurantCompositeIntegration;
import dev.kmunton.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantCompositeServiceImplementation {
    private ServiceUtil serviceUtil;
    private RestaurantCompositeIntegration integration;

    @Autowired
    public RestaurantCompositeServiceImplementation(ServiceUtil serviceUtil, RestaurantCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    private GeocodeDTO geocodeDTO;

    private List<RestaurantDTO> restaurantsResponse = new ArrayList<>();
    private Double chosenDistance;
    private SiteDTO site;
    private List<SiteDTO> sites = new ArrayList<>();


    public boolean isSiteInSites() {
        if (sites.isEmpty() != true) {
            site = sites.get(0);
            Integer id = site.getId();
            restaurantsResponse = integration.getRestaurantsBySiteId(id, String.valueOf(chosenDistance));
            return true;
        } else {
            return false;
        }
    }


    public boolean isRestaurantsAvailable() {
        if (restaurantsResponse.isEmpty() == true) {
            return false;
        } else if (restaurantsResponse.isEmpty() != true) {
            return true;
        } else {
            return false;
        }
    }

    public SiteDTO getSite(Integer id) {
        return integration.getSiteById(id);
    }

    public RestaurantDTO addRestaurant(RestaurantRequest req) {
        RestaurantDTO savedRes  = integration.addRestaurant(req);
        return savedRes;
    }

    public void getNewRestaurants() {
        String lat = String.valueOf(site.getLat());
        String lng = String.valueOf(site.getLng());
        Integer offset = 0;
        RestaurantResponse result = new RestaurantResponse();
        while (result.getData() == null || result.getData().isEmpty() != true) {
            result = integration.getRestaurantsPage(String.valueOf(offset), lat, lng);
            for (RestaurantJson res: result.getData()) {
                RestaurantRequest req = new RestaurantRequest(
                        res.getName(),
                        res.getDescription(),
                        res.getWebsite(),
                        res.getAddress(),
                        res.getDistance(),
                        site
                );
                RestaurantDTO savedRes  = addRestaurant(req);
                if (res.getDistance() <= chosenDistance) {
                    restaurantsResponse.add(savedRes);
                }

            }
            offset += 30;
        }
    }

    public void resetData() {
        restaurantsResponse.clear();
        sites.clear();
        site = null;
        chosenDistance = null;
    }


    public RestaurantsDTO getRestaurantsByOffice(String office, double distance) {
        resetData();
        chosenDistance = distance;
        sites = integration.getSitesByName(office);

        if (!isSiteInSites()) {
            geocodeDTO = integration.getLatLng(office);
            sites = integration.getSitesByLatLng(String.valueOf(geocodeDTO.getLat()), String.valueOf(geocodeDTO.getLng()));

            if (!isSiteInSites()) {
                SiteRequest siteReq = new SiteRequest(geocodeDTO.getName(), geocodeDTO.getLat(), geocodeDTO.getLng());
                site = integration.addSite(siteReq);
            }
        }
        if (!isRestaurantsAvailable()) {
            getNewRestaurants();
        }

        return new RestaurantsDTO(restaurantsResponse, site, chosenDistance);

    }




}
