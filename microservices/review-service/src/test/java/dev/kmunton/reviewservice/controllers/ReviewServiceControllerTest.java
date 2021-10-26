package dev.kmunton.reviewservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import dev.kmunton.reviewservice.repositories.ReviewRepository;
import dev.kmunton.reviewservice.services.ReviewServiceImplementation;
import dev.kmunton.testdatabase.PostgreSqlTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"eureka.client.enabled=false"})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/data.sql"})
@AutoConfigureMockMvc
class ReviewServiceControllerTest extends PostgreSqlTestBase {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ReviewRepository revRepo;

    @Autowired
    private ReviewServiceImplementation revService;


    private Integer restaurantId = 1;

    @Test
    void getReviewsByRestaurantId() {
        List<ReviewDTO> resp = client.exchange(String.format("/api/v1/database/reviews/%d", restaurantId), HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ReviewDTO>>() {
        }).getBody();
        assertEquals(true, resp.isEmpty());
    }

    @Test
    void addReview() throws Exception {
        RestaurantDTO res = new RestaurantDTO(
                1,
                "name",
                "",
                "",
                "address",
                0.1
        );
        ReviewRequest newReview = new ReviewRequest(5, "It is great", restaurantId);
        newReview.setRestaurant(res);
        String resp = this.mvc.perform(post("/api/v1/database/reviews")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(newReview)))
                .andReturn().getResponse().getContentAsString();

        assertEquals(true, resp.contains("\"id\":1"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
