package dev.kmunton.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hibernate_lazy_initializer"})
@Entity
@Table(name = "sites")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private double lat;
    private double lng;

    @OneToMany(mappedBy = "site", cascade = CascadeType.MERGE,
            orphanRemoval = true)
    @JsonIgnore
    private List<RestaurantToSite> restaurants = new ArrayList<>();

    public Site() {
    }

    public Site(String name, double lat, double lng, List<RestaurantToSite> restaurants) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.restaurants = restaurants;
    }

    public List<RestaurantToSite> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantToSite> restaurants) {
        this.restaurants = restaurants;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return Double.compare(site.lat, lat) == 0 &&
                Double.compare(site.lng, lng) == 0 &&
                id.equals(site.id) &&
                name.equals(site.name) &&
                restaurants.equals(site.restaurants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lat, lng, restaurants);
    }

    @Override
    public String toString() {
        return "Site{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", restaurants=" + restaurants +
                '}';
    }
}