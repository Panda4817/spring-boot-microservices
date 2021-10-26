package dev.kmunton.geocode.json;

import java.util.List;

public class GeocodeResponse {
    private List<Address> results;

    public List<Address> getResults() {
        return results;
    }

    public void setResults(List<Address> results) {
        this.results = results;
    }

    public Location getLocation() {
        return results.get(0).getLocation();
    }

    public String getAddress() {
        return results.get(0).getAddress();
    }
}
