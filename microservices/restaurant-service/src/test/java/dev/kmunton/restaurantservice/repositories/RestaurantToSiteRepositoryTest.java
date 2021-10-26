package dev.kmunton.restaurantservice.repositories;


import dev.kmunton.entities.RestaurantToSite;
import dev.kmunton.testdatabase.PostgreSqlTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/data.sql"})
class RestaurantToSiteRepositoryTest extends PostgreSqlTestBase {
    @Autowired
    private RestaurantToSiteRepository resToSiteRepo;

    @Autowired
    private RestaurantRepository restRepo;

    @Test
    void findBySite() {
        List <RestaurantToSite> result = resToSiteRepo.findAllBySite(1, 0.5);
        assertEquals(false, result.isEmpty());
    }
}