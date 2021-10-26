package dev.kmunton.joke.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.kmunton.api.core.joke.JokeDTO;
import dev.kmunton.joke.MockJokeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"eureka.client.enabled=false"})
class JokeServiceControllerTest {
    private Logger logger = LoggerFactory.getLogger(JokeServiceControllerTest.class);

    @Autowired
    private TestRestTemplate client;

    private MockRestServiceServer mockServer;

    @Autowired
    private MockJokeService mockService;

    @BeforeEach
    public void init() throws JsonProcessingException {
        logger.debug("Setting up mock server and expected results");
        mockServer = mockService.getMockServer();
    }

    @Test
    void getJoke() {
        logger.debug("Calling path using testRestTemplate");
        JokeDTO jokeDTO = client.getForObject("/api/v1/joke", JokeDTO.class);
        logger.debug("Verifying and asserting");
        mockServer.verify();
        assertEquals(mockService.getExpected(), jokeDTO);
    }
}