package dev.kmunton.restaurantservice.repositories;


import dev.kmunton.entities.Restaurant;
import dev.kmunton.testdatabase.PostgreSqlTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/data.sql"})
class RestaurantRepositoryTest extends PostgreSqlTestBase {
    @Autowired
    private RestaurantRepository restRepo;

    @Test
    void findById(){
        List<Restaurant> restaurants = restRepo.findAllById(1);
        assertEquals(false, restaurants.isEmpty());
    }
}