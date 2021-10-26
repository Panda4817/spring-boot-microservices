package dev.kmunton.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hibernate_lazy_initializer"})
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String website;
    private String address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.MERGE,
            orphanRemoval = true)
    @JsonIgnore
    private List<RestaurantToSite> sites = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.MERGE,
            orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(String name, String description, String website, String address) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.address = address;
    }

    public RestaurantToSite addSite(Site site, double distance) {
        RestaurantToSite resToSite = new RestaurantToSite(distance, this, site);
        sites.add(resToSite);
        site.getRestaurants().add(resToSite);
        return resToSite;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setRestaurant(this);
    }

    public void removeSite(Site site) {
        for (Iterator<RestaurantToSite> iterator = sites.iterator();
             iterator.hasNext(); ) {
            RestaurantToSite resToSite = iterator.next();

            if (resToSite.getRestaurant().equals(this) &&
                    resToSite.getSite().equals(site)) {
                iterator.remove();
                resToSite.getSite().getRestaurants().remove(resToSite);
                resToSite.setRestaurant(null);
                resToSite.setSite(null);
            }
        }
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setRestaurant(null);
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

    public List<RestaurantToSite> getSites() {
        return sites;
    }

    public void setSites(List<RestaurantToSite> sites) {
        this.sites = sites;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(website, that.website) &&
                address.equals(that.address) &&
                Objects.equals(sites, that.sites) &&
                Objects.equals(reviews, that.reviews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, website, address, sites, reviews);
    }
}