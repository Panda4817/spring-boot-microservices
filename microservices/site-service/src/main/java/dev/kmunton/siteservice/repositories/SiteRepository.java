package dev.kmunton.siteservice.repositories;

import dev.kmunton.entities.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
    List<Site> findByName(String name);

    @Query("SELECT s FROM Site s WHERE s.lat = :lat AND s.lng = :lng")
    List<Site> findByLatAndLng(@Param("lat") double lat, @Param("lng") double lng);

}
