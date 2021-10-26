package dev.kmunton.geocode.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.geocode.MockGeocodeService;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"eureka.client.enabled=false", "security.basic.enabled=false"})
class GeocodeServiceControllerTest {
    private Logger logger = LoggerFactory.getLogger(GeocodeServiceControllerTest.class);

    @Autowired
    private TestRestTemplate client;

    private MockRestServiceServer mockServer;

    @Autowired
    private MockGeocodeService mockService;

    @BeforeEach
    public void init() throws JsonProcessingException {
        logger.debug("Setting up mock server and expected results");
        mockServer = mockService.getMockServer();
    }

    @Test
    void getLatLng() {
        logger.debug("Calling path using testRestTemplate");
        GeocodeDTO geocodeDTO = client.getForObject(String.format("/api/v1/geocode?address=%s", mockService.getParam()), GeocodeDTO.class);
        System.out.println(geocodeDTO.toString());
        logger.debug("Verifying and asserting");
        mockServer.verify();
        assertEquals(mockService.getExpected(), geocodeDTO);
    }
}