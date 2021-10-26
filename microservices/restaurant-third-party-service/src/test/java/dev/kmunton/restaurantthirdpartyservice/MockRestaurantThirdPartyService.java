package dev.kmunton.restaurantthirdpartyservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.core.joke.JokeDTO;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantJson;
import dev.kmunton.api.core.restaurantThirdParty.RestaurantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Service
public class MockRestaurantThirdPartyService {
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private RestaurantResponse expected;
    private String base;
    private String lat;
    private String lng;
    private String offset;

    public MockRestServiceServer getMockServer() throws JsonProcessingException {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        expected = new RestaurantResponse();
        List<RestaurantJson> data = new ArrayList<>();
        RestaurantJson oneRest = new RestaurantJson();
        oneRest.setName("Best Restaurant");
        oneRest.setAddress("address");
        oneRest.setDistance(0.01);
        data.add(oneRest);
        expected.setData(data);
        base = "https://travel-advisor.p.rapidapi.com/restaurants/list-by-latlng";
        lat = "51.51379";
        lng = "-0.092817";
        offset = "0";
        mockServer.expect(ExpectedCount.once(),
                requestTo(String.format("%s?latitude=%s&longitude=%s&limit=30&currency=gbp&distance=0.5&open_now=false&offset=%s&lunit=mi&lang=en_gb", base, lat, lng, offset)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expected)));

        return mockServer;
    }

    public String getBase() {
        return base;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getOffset() {
        return offset;
    }

    public RestaurantResponse getExpected() {
        return expected;
    }
}

