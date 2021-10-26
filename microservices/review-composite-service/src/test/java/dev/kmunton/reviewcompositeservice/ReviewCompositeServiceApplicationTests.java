package dev.kmunton.reviewcompositeservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.composite.review.ReviewsDTO;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.review.ReviewDTO;
import dev.kmunton.api.core.review.ReviewRequest;
import dev.kmunton.reviewcompositeservice.integration.ReviewCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {"eureka.client.enabled=false"})
@AutoConfigureMockMvc
class ReviewCompositeServiceApplicationTests {
	private static final Integer RESTAURANT_ID = 1;
	private static final String ADDRESS = "address";
	private static final String RES_NAME = "Best Restaurant";
	private static final Double RES_DIST = 0.01;
	private static final Integer REVIEW_ID = 1;
	private ReviewRequest REQ = new ReviewRequest(5, "comment", RESTAURANT_ID);

	@Autowired
	private TestRestTemplate client;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ReviewCompositeIntegration revIntegration;

	@BeforeEach
	void setUp() {
		RestaurantDTO res = new RestaurantDTO(
				RESTAURANT_ID,
				RES_NAME,
				"",
				"",
				ADDRESS,
				RES_DIST
		);
		when(revIntegration.getRestaurantById(RESTAURANT_ID)).thenReturn(res);

		List<ReviewDTO> revs = new ArrayList<>();
		when(revIntegration.getReviewsByRestaurantId(RESTAURANT_ID)).thenReturn(revs);


		ReviewDTO rev = new ReviewDTO(REVIEW_ID, REQ.getRating(), REQ.getComment(), Instant.now());
		ReviewRequest request = REQ;
		request.setRestaurant(res);
		when(revIntegration.addReviewForRestaurant(request)).thenReturn(rev);
	}


	@Test
	void contextLoads() {
	}

	@Test
	void getReviewsByRestaurantId() {
		ReviewsDTO resp = client.getForObject(String.format("/api/v1/reviews/%d", RESTAURANT_ID), ReviewsDTO.class);
		assertEquals(true, resp.getData().isEmpty());
		assertEquals(RESTAURANT_ID, resp.getRestaurant().getId());
	}

	@Test
	void addReview() throws Exception {
		ReviewRequest rev = new ReviewRequest(5, "comment", RESTAURANT_ID);
		String resp = this.mvc.perform(post(String.format("/api/v1/reviews/%d", RESTAURANT_ID))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.with(csrf().asHeader())
				.content(asJsonString(rev)))
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
