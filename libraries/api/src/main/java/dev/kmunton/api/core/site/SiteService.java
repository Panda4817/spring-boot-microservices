package dev.kmunton.api.core.site;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface SiteService {
    @GetMapping(
            value    = "/api/v1/database/sites/name",
            produces = "application/json"
    )
    List<SiteDTO> getSitesByName(@RequestParam String name);

    @GetMapping(
            value    = "/api/v1/database/sites/latlng",
            produces = "application/json"
    )
    List<SiteDTO> getSitesByLatLng(@RequestParam String lat, @RequestParam String lng);

    @GetMapping(
            value    = "/api/v1/database/sites/{id}",
            produces = "application/json"
    )

    SiteDTO getSiteById(@PathVariable(name="id") Integer id);

    @PostMapping(
            value    = "/api/v1/database/sites",
            produces = "application/json"
    )
    SiteDTO addSite(@RequestBody SiteRequest request);
}
