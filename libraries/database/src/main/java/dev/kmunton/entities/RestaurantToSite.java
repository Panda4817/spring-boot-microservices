package dev.kmunton.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hibernate_lazy_initializer"})
@Entity
@Table(name = "restaurants_to_sites")
public class RestaurantToSite {
    @EmbeddedId
    private RestaurantSiteKey id;

    private double distance;


    @MapsId("restaurantId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Restaurant restaurant;


    @MapsId("siteId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Site site;

    public RestaurantToSite() {
    }

    public RestaurantToSite(double distance, Restaurant restaurant, Site site) {
        this.distance = distance;
        this.restaurant = restaurant;
        this.site = site;
        this.id = new RestaurantSiteKey(restaurant.getId(), site.getId());
    }

    public RestaurantSiteKey getId() {
        return id;
    }

    public void setId(RestaurantSiteKey id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantToSite that = (RestaurantToSite) o;
        return Double.compare(that.distance, distance) == 0 &&
                id.equals(that.id) &&
                restaurant.equals(that.restaurant) &&
                site.equals(that.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, restaurant, site);
    }

    @Override
    public String toString() {
        return "RestaurantToSite{" +
                "id=" + id +
                ", distance=" + distance +
                ", restaurant=" + restaurant +
                ", site=" + site +
                '}';
    }
}