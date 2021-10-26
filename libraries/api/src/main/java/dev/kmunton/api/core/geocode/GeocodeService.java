package dev.kmunton.api.core.geocode;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface GeocodeService {

    @GetMapping(
            value    = "/api/v1/geocode",
            produces = "application/json"
    )
    GeocodeDTO getLatLng(@RequestParam String address);
}
