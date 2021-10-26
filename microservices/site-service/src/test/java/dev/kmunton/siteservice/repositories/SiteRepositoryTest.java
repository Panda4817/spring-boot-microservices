package dev.kmunton.siteservice.repositories;



import dev.kmunton.entities.Site;
import dev.kmunton.testdatabase.PostgreSqlTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
@Sql({"/data.sql"})
class SiteRepositoryTest extends PostgreSqlTestBase {
    @Autowired
    private SiteRepository siteRepo;

    private Integer siteId = 1;

    @Test
    public void findByName() {
        List<Site> result = siteRepo.findByName("name");
        Integer count = result.size();
        assertEquals(1, count);
    }

    @Test
    public void findByLatAndLng() {
        List<Site> result = siteRepo.findByLatAndLng(5.1, 2.1);
        Integer count = result.size();
        assertEquals(1, count);
    }

    @Test
    public void getSiteById() {
        Optional<Site> site = siteRepo.findById(siteId);
        assertEquals(false, site.isEmpty());
    }
}