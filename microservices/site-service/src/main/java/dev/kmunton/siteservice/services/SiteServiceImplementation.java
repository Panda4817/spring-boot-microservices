package dev.kmunton.siteservice.services;

import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.api.core.site.SiteRequest;
import dev.kmunton.entities.Site;
import dev.kmunton.siteservice.repositories.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SiteServiceImplementation {

    @Autowired
    private SiteRepository siteRepo;

    public Site getSiteById(Integer id) {
        Optional<Site> site = siteRepo.findById(id);
        if (site.isEmpty()) {
            return new Site();
        }
        return site.get();
    }

    public List<Site> getSitesByName(String name) {
        return siteRepo.findByName(name);
    }

    public List<Site> getSitesByLatLng(Double lat, Double lng) {
        return siteRepo.findByLatAndLng(lat, lng);
    }


    public Site saveSite(SiteRequest site) {
        Site newSite = new Site(site.getName(), site.getLat(), site.getLng(), new ArrayList<>());
        newSite = siteRepo.save(newSite);
        return newSite;
    }

    public List<SiteDTO> prepareSiteList(List<Site> sites) {
        List<SiteDTO> sitesResponse = new ArrayList<>();
        for (Site s: sites) {
            SiteDTO site = new SiteDTO(
                    s.getId(),
                    s.getName(),
                    s.getLat(),
                    s.getLng()
            );
            sitesResponse.add(site);
        }
        return sitesResponse;
    }
}
