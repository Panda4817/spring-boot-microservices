package dev.kmunton.joke;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.core.joke.JokeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@Service
public class MockJokeService {
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private JokeDTO expected;
    private String param;

    public MockRestServiceServer getMockServer() throws JsonProcessingException {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        expected = new JokeDTO();
        expected.setId("1");
        expected.setJoke("Why does Wally only wear stripes? Because he doesn't want to be spotted.");
        expected.setStatus("200");

        mockServer.expect(ExpectedCount.once(),
                requestTo("https://icanhazdadjoke.com/"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expected)));

        return mockServer;
    }

    public JokeDTO getExpected() {
        return expected;
    }

    public String getParam() {
        return param;
    }
}
