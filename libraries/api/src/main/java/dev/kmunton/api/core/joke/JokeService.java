package dev.kmunton.api.core.joke;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name="Lunchtime jokes", description="API to return a random dad joke")
public interface JokeService {

    @Operation(
            summary =
                    "${api.joke.get-random-joke.description}",
            description =
                    "${api.joke.get-random-joke.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =
                    "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "404", description =
                    "${api.responseCodes.notFound.description}"),
    })
    @GetMapping(
            value    = "/api/v1/joke",
            produces = "application/json"
    )
    JokeDTO getJoke();

}