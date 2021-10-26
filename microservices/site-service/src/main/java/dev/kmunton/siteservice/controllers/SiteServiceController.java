package dev.kmunton.siteservice.controllers;

import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.api.core.site.SiteRequest;
import dev.kmunton.api.core.site.SiteService;
import dev.kmunton.api.exceptions.InvalidInputException;
import dev.kmunton.api.exceptions.NotFoundException;
import dev.kmunton.entities.Site;
import dev.kmunton.siteservice.services.SiteServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SiteServiceController implements SiteService {
    private static final Logger LOG = LoggerFactory.getLogger(SiteServiceController.class);

    @Autowired
    private SiteServiceImplementation dbService;

    @Override
    public List<SiteDTO> getSitesByName(String name) {
        LOG.debug("/sites?name={} returns sites based on name", name);
        List<Site> sites = dbService.getSitesByName(name);
        return dbService.prepareSiteList(sites);
    }

    @Override
    public List<SiteDTO> getSitesByLatLng(String lat, String lng) {
        LOG.debug("/sites?lat={}&lng={} returns sites based on lat and lng", lat, lng);
        Double latitude = Double.valueOf(lat);
        Double longitude = Double.valueOf(lng);

        List<Site> sites = dbService.getSitesByLatLng(latitude, longitude);
        return dbService.prepareSiteList(sites);

    }

    @Override
    public SiteDTO getSiteById(Integer id) {
        LOG.debug("/sites/{} return a site with id ", id);
        if (id < 1) {
            throw new InvalidInputException("Invalid site id: " + id);
        }
        Site site = dbService.getSiteById(id);
        if (site.getId() == null) {
            throw new NotFoundException("No site found with id: " + id);
        }
        return new SiteDTO(site.getId(), site.getName(), site.getLat(), site.getLng());
    }

    @Override
    public SiteDTO addSite(SiteRequest request) {
        LOG.debug("/sites save a site and return new site with id");
        Site site = dbService.saveSite(request);
        return new SiteDTO(
                site.getId(),
                site.getName(),
                site.getLat(),
                site.getLng()
        );
    }
}
