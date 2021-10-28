package dev.kmunton.restaurantthirdpartyservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantJson;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import dev.kmunton.restaurantthirdpartyservice.MockRestaurantThirdPartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestaurantThirdPartyServiceImplementationTest {
    private Logger logger = LoggerFactory.getLogger(RestaurantThirdPartyServiceImplementationTest.class);

    @Autowired
    private RestaurantThirdPartyServiceImplementation service;

    private MockRestServiceServer mockServer;

    @Autowired
    private MockRestaurantThirdPartyService mockService;

    @BeforeEach
    public void init() throws JsonProcessingException {
        logger.debug("Setting up mock server and expected results");
        mockServer = mockService.getMockServer();
    }

    @Test
    void getRestaurantsAroundBass() {

        logger.debug("Calling method from service");
        RestaurantResponse res = service.getRestaurantsPerPage("0", "51.51379", "-0.092817");
        logger.debug("Verifying and asserting");
        mockServer.verify();
        assertEquals(mockService.getExpected(), res);
        assertNotNull(res.getData());
        for (RestaurantJson rest : res.getData()) {
            assertNotNull(rest.getName());
            assertNotNull(rest.getDistance());
            assertNotNull(rest.getAddress());
        }
    }
}