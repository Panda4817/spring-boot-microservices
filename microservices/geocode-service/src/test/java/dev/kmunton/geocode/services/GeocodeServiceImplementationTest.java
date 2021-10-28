package dev.kmunton.geocode.services;


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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GeocodeServiceImplementationTest {
    private Logger logger = LoggerFactory.getLogger(GeocodeServiceImplementationTest.class);

    @Autowired
    private GeocodeServiceImplementation service;

    private MockRestServiceServer mockServer;

    @Autowired
    private MockGeocodeService mockService;

    @BeforeEach
    public void init() throws JsonProcessingException {
        logger.debug("Setting up mock server and expected results");
        mockServer = mockService.getMockServer();
    }

    @Test
    void getLatLngLondonBjss() {

        logger.debug("Calling method from service");
        GeocodeDTO bjss = service.getLatLng("1 Crown Court London EC2V 6JP");
        logger.debug("Verifying and asserting");
        mockServer.verify();
        assertEquals(mockService.getExpected(), bjss);
        assertEquals(51.51379, bjss.getLat(), 0.01);
        assertEquals(-0.092817, bjss.getLng(), 0.01);
    }

}