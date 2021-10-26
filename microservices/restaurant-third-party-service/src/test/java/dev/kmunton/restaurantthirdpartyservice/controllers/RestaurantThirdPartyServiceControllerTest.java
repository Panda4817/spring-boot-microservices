package dev.kmunton.restaurantthirdpartyservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import dev.kmunton.restaurantthirdpartyservice.MockRestaurantThirdPartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"eureka.client.enabled=false"})
class RestaurantThirdPartyServiceControllerTest {

    private Logger logger = LoggerFactory.getLogger(RestaurantThirdPartyServiceControllerTest.class);

    @Autowired
    private TestRestTemplate client;

    private MockRestServiceServer mockServer;

    @Autowired
    private MockRestaurantThirdPartyService mockService;

    @BeforeEach
    public void init() throws JsonProcessingException {
        logger.debug("Setting up mock server and expected results");
        mockServer = mockService.getMockServer();
    }

    @Test
    void getRestaurantsPage() {
        logger.debug("Calling path using testRestTemplate");
        RestaurantResponse resp = client.getForObject(
                String.format("/api/v1/thirdparty/restaurants?offset=%s&lat=%s&lng=%s", mockService.getOffset(), mockService.getLat(), mockService.getLng()),
                RestaurantResponse.class);
        logger.debug("Verifying and asserting");
        mockServer.verify();
        assertEquals(mockService.getExpected(), resp);
    }
}