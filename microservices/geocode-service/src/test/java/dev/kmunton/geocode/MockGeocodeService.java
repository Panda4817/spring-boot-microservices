package dev.kmunton.geocode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.geocode.json.Address;
import dev.kmunton.geocode.json.GeocodeResponse;
import dev.kmunton.geocode.json.Location;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Service
public class MockGeocodeService {

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private GeocodeDTO expected;
    private String param;

    public MockRestServiceServer getMockServer() throws JsonProcessingException {
        mockServer = MockRestServiceServer.createServer(restTemplate);

        String name = "1 Crown Court London EC2V 6JP";
        expected = new GeocodeDTO(name, 51.51379, -0.092817);
        GeocodeResponse resp = new GeocodeResponse();
        List<Address> results = new ArrayList<>();
        Address address = new Address();
        Location loc = new Location();
        address.setAddress("1 Crown Court London EC2V 6JP");
        loc.setLat(51.51379);
        loc.setLng(-0.092817);
        address.setLocation(loc);
        results.add(address);
        resp.setResults(results);
        param = URLEncoder.encode(name, StandardCharsets.UTF_8);

        mockServer.expect(ExpectedCount.once(),
                requestTo(String.format("https://trueway-geocoding.p.rapidapi.com/Geocode?address=%s", param)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(resp)));

        return mockServer;
    }

    public GeocodeDTO getExpected() {
        return expected;
    }

    public String getParam(){
        return param;
    }




}

