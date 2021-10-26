package dev.kmunton.api.core.restaurant;

import dev.kmunton.api.core.site.SiteDTO;

import java.util.Objects;

public class RestaurantRequest {
    private String name;
    private String description;
    private String website;
    private String address;

    private double distance;
    private SiteDTO site;

    public RestaurantRequest() {
    }

    public RestaurantRequest(String name, String description, String website, String address, double distance, SiteDTO site) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.address = address;
        this.distance = distance;
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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
        RestaurantRequest that = (RestaurantRequest) o;
        return Double.compare(that.distance, distance) == 0 &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(website, that.website) &&
                address.equals(that.address) &&
                Objects.equals(site, that.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, website, address, distance, site);
    }

    @Override
    public String toString() {
        return "RestaurantRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", address='" + address + '\'' +
                ", distance=" + distance +
                ", site=" + site +
                '}';
    }
}
