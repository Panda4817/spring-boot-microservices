package dev.kmunton.joke.controllers;

import dev.kmunton.api.core.joke.JokeDTO;
import dev.kmunton.api.core.joke.JokeService;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.joke.services.JokeServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeServiceController implements JokeService {
    private static final Logger LOG = LoggerFactory.getLogger(JokeServiceController.class);

    @Autowired
    private JokeServiceImplementation jokeService;

    @Override
    public JokeDTO getJoke() {
        LOG.debug("/joke returns random joke");
        JokeDTO jokeDTO = jokeService.getRandomJoke();
        LOG.info(jokeDTO.toString());
        if (Integer.valueOf(jokeDTO.getStatus()) != 200) {
            throw new NotFoundException("No Joke found :( icanhazdadjoke.com seems to be down.");
        }
        return jokeDTO;
    }
}
