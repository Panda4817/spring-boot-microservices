package dev.kmunton.api.core.restaurantThirdParty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantResponse {
    private List<RestaurantJson> data;

    public List<RestaurantJson> getData() {
        return data;
    }

    public void setData(List<RestaurantJson> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String dataString = "";
        for (RestaurantJson res : data) {
            dataString += res.toString();
        }
        return String.format("RestaurentResponse{data=%s}", dataString);
    }

    public void removeNulls() {
        List<RestaurantJson> filteredData = new ArrayList<RestaurantJson>();
        for (RestaurantJson res : data) {
            if (res.getName() != null) {
                filteredData.add(res);
            }
        }
        data = filteredData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantResponse that = (RestaurantResponse) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
