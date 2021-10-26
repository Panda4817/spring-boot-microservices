package dev.kmunton.api.composite;

public class serviceAddresses {
    private final String composite;
    private final String geocode;
    private final String joke;
    private final String restaurant;

    public serviceAddresses() {
        this.composite = null;
        this.geocode = null;
        this.joke = null;
        this.restaurant = null;
    }

    public serviceAddresses(String composite, String geocode, String joke, String restaurant) {
        this.composite = composite;
        this.geocode = geocode;
        this.joke = joke;
        this.restaurant = restaurant;
    }

    public String getComposite() {
        return composite;
    }

    public String getGeocode() {
        return geocode;
    }

    public String getJoke() {
        return joke;
    }

    public String getRestaurant() {
        return restaurant;
    }
}
