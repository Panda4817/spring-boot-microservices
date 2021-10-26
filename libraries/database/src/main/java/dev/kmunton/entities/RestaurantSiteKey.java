package dev.kmunton.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RestaurantSiteKey implements Serializable {

    @Column(name = "restaurant_id")
    Integer restaurantId;

    @Column(name = "site_id")
    Integer siteId;

    public RestaurantSiteKey() {
    }

    public RestaurantSiteKey(Integer restaurantId, Integer siteId) {
        this.restaurantId = restaurantId;
        this.siteId = siteId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantSiteKey that = (RestaurantSiteKey) o;
        return restaurantId.equals(that.restaurantId) &&
                siteId.equals(that.siteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, siteId);
    }

    @Override
    public String toString() {
        return "RestaurantSiteKey{" +
                "restaurantId=" + restaurantId +
                ", siteId=" + siteId +
                '}';
    }
}
