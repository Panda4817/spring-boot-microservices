package dev.kmunton.restaurantthirdpartyservice.services;

import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestaurantThirdPartyServiceImplementation {
    private static final String BASE = "https://travel-advisor.p.rapidapi.com/restaurants/list-by-latlng";
    private static final String KEY = System.getenv("API_KEY");
    private static final String HOST = "travel-advisor.p.rapidapi.com";

    @Autowired
    private RestTemplate restTemplate;

    public RestaurantResponse getRestaurantsPerPage(String offset, String lat, String lng) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-host", HOST);
        headers.add("x-rapidapi-key", KEY);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<RestaurantResponse> response = restTemplate.exchange(String.format("%s?latitude=%s&longitude=%s&limit=30&currency=gbp&distance=0.5&open_now=false&offset=%s&lunit=mi&lang=en_gb", BASE, lat, lng, offset), HttpMethod.GET, request, RestaurantResponse.class);
        RestaurantResponse body = response.getBody();
        body.removeNulls();
        return body;
    }
}
