package dev.kmunton.api.core.site;

import java.util.Objects;

public class SiteDTO {
    private Integer id;
    private String name;
    private double lat;
    private double lng;

    public SiteDTO() {
    }

    public SiteDTO(Integer id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
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
        SiteDTO siteDTO = (SiteDTO) o;
        return Double.compare(siteDTO.lat, lat) == 0 &&
                Double.compare(siteDTO.lng, lng) == 0 &&
                Objects.equals(id, siteDTO.id) &&
                Objects.equals(name, siteDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lat, lng);
    }

    @Override
    public String toString() {
        return "SiteDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
