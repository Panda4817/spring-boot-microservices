package dev.kmunton.api.core.restaurant;

import java.util.Objects;

public class RestaurantDTO {
    private Integer id;
    private String name;
    private String description;
    private String website;
    private String address;

    private double distance;

    public RestaurantDTO() {
    }

    public RestaurantDTO(Integer id, String name, String description, String website, String address, double distance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.address = address;
        this.distance = distance;
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

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", website='" + website + '\'' +
                ", address='" + address + '\'' +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDTO that = (RestaurantDTO) o;
        return Double.compare(that.distance, distance) == 0 &&
                id.equals(that.id) &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(website, that.website) &&
                address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, website, address, distance);
    }
}
