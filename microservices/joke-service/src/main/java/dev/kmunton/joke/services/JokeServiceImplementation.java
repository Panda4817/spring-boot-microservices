package dev.kmunton.joke.services;

import dev.kmunton.api.core.joke.JokeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JokeServiceImplementation {
    private static final String BASE = "https://icanhazdadjoke.com/";

    @Autowired
    private RestTemplate restTemplate;


    public JokeDTO getRandomJoke() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Java Spring Boot Joke Microservice - Lunchtime application");
        headers.add("Accept", "application/json");
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<JokeDTO> response = restTemplate.exchange(BASE, HttpMethod.GET, request, JokeDTO.class);
        JokeDTO body = response.getBody();
        return body;
    }
}
