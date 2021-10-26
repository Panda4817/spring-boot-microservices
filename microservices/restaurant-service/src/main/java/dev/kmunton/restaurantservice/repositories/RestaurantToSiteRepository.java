package dev.kmunton.restaurantservice.repositories;

import dev.kmunton.entities.RestaurantSiteKey;
import dev.kmunton.entities.RestaurantToSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantToSiteRepository extends JpaRepository<RestaurantToSite, RestaurantSiteKey> {
    @Query("SELECT rs FROM RestaurantToSite rs INNER JOIN rs.site s WHERE s.id = :id AND rs.distance <= :distance ORDER BY rs.distance ASC")
    List<RestaurantToSite> findAllBySite(@Param("id") Integer id, @Param("distance") Double distance);
}
