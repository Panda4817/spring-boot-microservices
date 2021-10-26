package dev.kmunton.reviewcompositeservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import dev.kmunton.api.core.restaurant.RestaurantService;
import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import dev.kmunton.api.core.review.ReviewService;
import dev.kmunton.api.exceptions.BadRequestException;
import dev.kmunton.api.exceptions.InvalidInputException;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.util.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
public class ReviewCompositeIntegration implements RestaurantService, ReviewService {
    private static final Logger LOG = LoggerFactory.getLogger(ReviewCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String restaurantServiceUrl = "http://restaurant";
    private final String reviewServiceUrl = "http://review";

    @Autowired
    public ReviewCompositeIntegration(
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
    public List<ReviewDTO> getReviewsByRestaurantId(Integer id) {
        try {
            String url = String.format("%s/api/v1/database/reviews/%d", reviewServiceUrl, id);
            LOG.debug("Will call getReviewsByRestaurantId API on URL={}", url);
            List<ReviewDTO> response = restTemplate.exchange(url, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<ReviewDTO>>() {}).getBody();
            LOG.debug("Returning review results for restaurant");
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
    public ReviewDTO addReviewForRestaurant(@NotNull ReviewRequest request) {
        try{
            String url = String.format("%s/api/v1/database/reviews", reviewServiceUrl);
            LOG.debug("Will call addReviewForRestaurant API on URL={}", url);
            ReviewDTO review = restTemplate.postForObject(url, request, ReviewDTO.class);
            LOG.debug("Restaurant saved into database");
            return review;
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
}
