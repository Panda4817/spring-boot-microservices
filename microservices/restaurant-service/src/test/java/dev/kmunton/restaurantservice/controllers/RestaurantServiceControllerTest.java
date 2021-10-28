package dev.kmunton.restaurantservice.controllers;

import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.testdatabase.PostgreSqlTestBase;
import dev.kmunton.restaurantservice.repositories.RestaurantRepository;
import dev.kmunton.restaurantservice.services.RestaurantServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/data.sql"})
public class RestaurantServiceControllerTest extends PostgreSqlTestBase {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private RestaurantRepository restRepo;

    @Autowired
    private RestaurantServiceImplementation resService;

    private Integer siteId = 1;
    private Integer restaurantId = 1;

    @Test
    void getRestaurantsBySiteId() {
        List<RestaurantDTO> resp = client.exchange(String.format("/api/v1/database/restaurants/%d?distance=0.5", siteId), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RestaurantDTO>>() {}).getBody();;
        assertEquals(false, resp.isEmpty());
    }

    @Test
    void getRestaurantById() {
        RestaurantDTO resp = client.getForObject(String.format("/api/v1/database/restaurant/%d", restaurantId), RestaurantDTO.class);
        assertEquals(restaurantId, resp.getId());
    }

}
