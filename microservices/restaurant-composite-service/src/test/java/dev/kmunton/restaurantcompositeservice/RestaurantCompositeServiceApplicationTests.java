package dev.kmunton.restaurantcompositeservice;

import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.composite.restaurant.RestaurantsDTO;
import dev.kmunton.api.core.restaurant.RestaurantRequest;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantJson;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.api.core.site.SiteRequest;
import dev.kmunton.restaurantcompositeservice.integration.RestaurantCompositeIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantCompositeServiceApplicationTests {
	private static final Integer RESTAURANT_ID = 1;
	private static final Integer SITE_ID = 1;
	private static final String ADDRESS = "address";
	private static final Double LAT = 5.0;
	private static final Double LNG = 1.0;
	private static final String RES_NAME = "Best Restaurant";
	private static final Double RES_DIST = 0.01;
	private static final Double CHOSEN_DIST = 0.3;

	@Autowired
	private TestRestTemplate client;

	@MockBean
	private RestaurantCompositeIntegration restIntegration;

	@BeforeEach
	void setUp() {
		GeocodeDTO geocodeDTO = new GeocodeDTO();
		geocodeDTO.setName(ADDRESS);
		geocodeDTO.setLat(LAT);
		geocodeDTO.setLng(LNG);
		when(restIntegration.getLatLng(ADDRESS)).thenReturn(geocodeDTO);

		SiteRequest siteReq = new SiteRequest(geocodeDTO.getName(), geocodeDTO.getLat(), geocodeDTO.getLng());
		SiteDTO siteDTO = new SiteDTO(SITE_ID, siteReq.getName(), siteReq.getLat(), siteReq.getLng());
		when(restIntegration.addSite(siteReq)).thenReturn(siteDTO);
		when(restIntegration.getSiteById(SITE_ID)).thenReturn(siteDTO);

		List<SiteDTO> listSiteDTO = new ArrayList<>();
		when(restIntegration.getSitesByLatLng("5.0", "1.0")).thenReturn(listSiteDTO);
		when(restIntegration.getSitesByName(ADDRESS)).thenReturn(listSiteDTO);


		RestaurantResponse resResp = new RestaurantResponse();
		List<RestaurantJson> data = new ArrayList<>();
		RestaurantJson oneRest = new RestaurantJson();
		oneRest.setName(RES_NAME);
		oneRest.setAddress(ADDRESS);
		oneRest.setDistance(RES_DIST);
		data.add(oneRest);
		resResp.setData(data);
		when(restIntegration.getRestaurantsPage("0", "5.0", "1.0")).thenReturn(resResp);

		RestaurantResponse resRespEmpty = new RestaurantResponse();
		resRespEmpty.setData(new ArrayList<RestaurantJson>());
		when(restIntegration.getRestaurantsPage("30", "5.0", "1.0")).thenReturn(resRespEmpty);

		RestaurantRequest resReq = new RestaurantRequest(
				oneRest.getName(),
				oneRest.getDescription(),
				oneRest.getWebsite(),
				oneRest.getAddress(),
				oneRest.getDistance(),
				siteDTO
		);
		RestaurantDTO restDTO = new RestaurantDTO(
				RESTAURANT_ID,
				resReq.getName(),
				resReq.getDescription(),
				resReq.getWebsite(),
				resReq.getAddress(),
				resReq.getDistance()
		);
		when(restIntegration.addRestaurant(resReq)).thenReturn(restDTO);
		when(restIntegration.getRestaurantById(RESTAURANT_ID)).thenReturn(restDTO);

		RestaurantsDTO restResp = new RestaurantsDTO();
		List<RestaurantDTO> listRest = new ArrayList<>();
		restResp.setData(listRest);
		restResp.setSite(siteDTO);
		restResp.setChosenDistance(CHOSEN_DIST);
		when(restIntegration.getRestaurantsBySiteId(SITE_ID, "0.3")).thenReturn(listRest);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getRestaurantsByOffice() {
		RestaurantsDTO resp = client.getForObject(String.format("/api/v1/restaurants?office=%s&distance=%s", ADDRESS, "0.3"), RestaurantsDTO.class);
		assertEquals(SITE_ID, resp.getSite().getId());
		assertEquals(RESTAURANT_ID, resp.getData().get(0).getId());
	}



}
