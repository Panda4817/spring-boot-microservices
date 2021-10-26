package dev.kmunton.api.composite.restaurant;


import dev.kmunton.api.core.restaurant.RestaurantDTO;
import dev.kmunton.api.core.site.SiteDTO;

import java.util.List;
import java.util.Objects;

public class RestaurantsDTO {
    private List<RestaurantDTO> data;
    private double chosenDistance;
    private SiteDTO site;

    public RestaurantsDTO() {
    }

    public RestaurantsDTO(List<RestaurantDTO> data, SiteDTO site, double distance) {
        this.data = data;
        this.site = site;
        chosenDistance = distance;
    }

    public List<RestaurantDTO> getData() {
        return data;
    }

    public void setData(List<RestaurantDTO> data) {
        this.data = data;
    }

    public double getChosenDistance() {
        return chosenDistance;
    }

    public void setChosenDistance(double chosenDistance) {
        this.chosenDistance = chosenDistance;
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantsDTO that = (RestaurantsDTO) o;
        return Double.compare(that.chosenDistance, chosenDistance) == 0 &&
                Objects.equals(data, that.data) &&
                Objects.equals(site, that.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, chosenDistance, site);
    }

    @Override
    public String toString() {
        return "RestaurantsDTO{" +
                "data=" + data +
                ", chosenDistance=" + chosenDistance +
                ", site=" + site +
                '}';
    }
}