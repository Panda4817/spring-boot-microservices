package dev.kmunton.restaurantcompositeservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.api.core.geocode.GeocodeService;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import dev.kmunton.api.core.restaurant.RestaurantService;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantThirdPartyService;
import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.api.core.site.SiteRequest;
import dev.kmunton.api.core.site.SiteService;
import dev.kmunton.api.exceptions.BadRequestException;
import dev.kmunton.api.exceptions.InvalidInputException;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.util.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantCompositeIntegration implements GeocodeService, RestaurantThirdPartyService, RestaurantService, SiteService {
    private static final Logger LOG = LoggerFactory.getLogger(RestaurantCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String geocodeServiceUrl = "http://geocode";
    private final String restaurantThirdPartyServiceUrl = "http://restaurant-thirdparty";
    private final String restaurantServiceUrl = "http://restaurant";
    private final String siteServiceUrl = "http://site";

    @Autowired
    public RestaurantCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper mapper
    ) {

        this.restTemplate = restTemplate;
        this.mapper = mapper;

    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            String info = mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
            return info;
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }


    @Override
    public GeocodeDTO getLatLng(String address) {
        try {
            String url = String.format("%s/api/v1/geocode?address=%s", geocodeServiceUrl, address);
            LOG.debug("Will call getLatLng API on URL: {}", url);
            GeocodeDTO site = restTemplate.getForObject(url, GeocodeDTO.class);
            LOG.debug("Found a site info for address={} with lat and lng", site.getName());
            return site;

        } catch(HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }

    }

    @Override
    public RestaurantResponse getRestaurantsPage(String offset, String lat, String lng) {
        try {
            String url = String.format("%s/api/v1/thirdparty/restaurants?offset=%s&lat=%s&lng=%s", restaurantThirdPartyServiceUrl, offset, lat, lng);
            LOG.debug("Will call getRestaurantsPage API on URL={} for a page of 30 results", url);
            RestaurantResponse restaurants = restTemplate.getForObject(url, RestaurantResponse.class);
            LOG.debug("Returning results for restaurants");
            return restaurants;
        } catch(Exception ex) {
            LOG.warn("Got an exception while requesting restaurants, return zero restaurants: {}", ex.getMessage());
            RestaurantResponse resp = new RestaurantResponse();
            resp.setData(new ArrayList<>());
            return resp;
        }

    }

    @Override
    public List<RestaurantDTO> getRestaurantsBySiteId(Integer id, String distance) {
        try {
            String url = String.format("%s/api/v1/database/restaurants/%d?distance=%s", restaurantServiceUrl, id, distance);
            LOG.debug("Will call getRestaurantsBySiteId API on URL={}", url);
            List<RestaurantDTO> response = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<RestaurantDTO>>() {}).getBody();
            LOG.debug("Returning results for restaurants");
            return response;
        }catch(HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }


    }

    @Override
    public RestaurantDTO getRestaurantById(Integer id) {
        try {
            String url = String.format("%s/api/v1/database/restaurant/%d", restaurantServiceUrl, id);
            LOG.debug("Will call getRestaurantsById API on URL={}", url);
            RestaurantDTO restaurant = restTemplate.getForObject(url, RestaurantDTO.class);
            LOG.debug("Returning one restaurant");
            return restaurant;
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST:
                    throw new BadRequestException(getErrorMessage(ex));

                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }

    }

    @Override
    public RestaurantDTO addRestaurant(RestaurantRequest request) {
        try{
            String url = String.format("%s/api/v1/database/restaurants", restaurantServiceUrl);
            LOG.debug("Will call addRestaurant API on URL={}", url);
            RestaurantDTO res = restTemplate.postForObject(url, request, RestaurantDTO.class);
            LOG.debug("Restaurant saved into database");
            return res;
        } catch(HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST:
                    throw new BadRequestException(getErrorMessage(ex));

                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }

    }

    @Override
    public List<SiteDTO> getSitesByName(String name) {
        try {
            String url = String.format("%s/api/v1/database/sites/name?name=", siteServiceUrl, name);
            LOG.debug("Will call getSitesByName API on URL={}", url);
            List<SiteDTO> sites = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<SiteDTO>>() {}).getBody();
            LOG.debug("Return list of matching sites from database");
            return sites;
        }catch (Exception ex) {
            LOG.warn("Got an exception while requesting sites from database, return zero sites: {}", ex.getMessage());
            return new ArrayList<>();
        }

    }

    @Override
    public List<SiteDTO> getSitesByLatLng(String lat, String lng) {
        try {
            String url = String.format("%s/api/v1/database/sites/latlng?lat=%s&lng=%s", siteServiceUrl, lat, lng);
            LOG.debug("Will call getSitesByLatLng API on URL={}", url);
            List<SiteDTO> sites = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<SiteDTO>>() {}).getBody();
            LOG.debug("Return list of matching sites from database");
            return sites;
        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting sites from database, return zero sites: {}", ex.getMessage());
            return new ArrayList<>();
        }


    }

    @Override
    public SiteDTO getSiteById(Integer id) {
        try {
            String url = String.format("%s/api/v1/database/sites/%d", siteServiceUrl, id);
            LOG.debug("Will call addSite API on URL={}", url);
            SiteDTO site = restTemplate.getForObject(url, SiteDTO.class);
            LOG.debug("Site retrieved from sites table");
            return site;
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST:
                    throw new BadRequestException(getErrorMessage(ex));

                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }

        }
    }

    @Override
    public SiteDTO addSite(SiteRequest request) {
        try {
            String url = String.format("%s/api/v1/database/sites", siteServiceUrl);
            LOG.debug("Will call addSite API on URL={}", url);
            SiteDTO site = restTemplate.postForObject(url, request, SiteDTO.class);
            LOG.debug("Site saved to database and saved site returned");
            return site;
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST:
                    throw new BadRequestException(getErrorMessage(ex));

                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(ex));

                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }

        }

    }
}
