package dev.kmunton.geocode.services;

import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.geocode.json.GeocodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodeServiceImplementation {
    private static final String BASE = "https://trueway-geocoding.p.rapidapi.com/Geocode";
    private static final String KEY = System.getenv("API_KEY");
    private static final String HOST = "trueway-geocoding.p.rapidapi.com";

    @Autowired
    private RestTemplate restTemplate;


    public GeocodeDTO getLatLng(String address) {
        String encodedAddress = "";
        encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-rapidapi-host", HOST);
        headers.add("x-rapidapi-key", KEY);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<GeocodeResponse> response = restTemplate.exchange(String.format("%s?address=%s", BASE, encodedAddress), HttpMethod.GET, request, GeocodeResponse.class);
        GeocodeResponse body = response.getBody();
        if (body.getResults().isEmpty()) {
            return new GeocodeDTO();
        }
        return new GeocodeDTO(body.getAddress(), body.getLocation().getLat(), body.getLocation().getLng());

    }

}