package dev.kmunton.geocode.controllers;

import dev.kmunton.api.core.geocode.GeocodeService;
import dev.kmunton.api.core.geocode.GeocodeDTO;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.geocode.services.GeocodeServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeocodeServiceController implements GeocodeService {

    private static final Logger LOG = LoggerFactory.getLogger(GeocodeServiceController.class);

    @Autowired
    private GeocodeServiceImplementation geoService;

    @Override
    public GeocodeDTO getLatLng(String address) {
        LOG.debug("/geocode?address={} return lat and lng for an address", address);
        GeocodeDTO siteInfo = geoService.getLatLng(address);
        if (siteInfo.getName() == null) {
            throw new NotFoundException("No lat or lng could be found for address: " + address);
        }
        return siteInfo;
    }
}
