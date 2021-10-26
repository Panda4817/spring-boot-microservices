package dev.kmunton.siteservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kmunton.api.core.site.SiteDTO;
import dev.kmunton.api.core.site.SiteRequest;
import dev.kmunton.siteservice.repositories.SiteRepository;
import dev.kmunton.siteservice.services.SiteServiceImplementation;
import dev.kmunton.testdatabase.PostgreSqlTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"eureka.client.enabled=false"})
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@Sql({"/data.sql"})
public class SiteServiceControllerTest extends PostgreSqlTestBase {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SiteRepository siteRepo;


    @Autowired
    private SiteServiceImplementation siteService;

    private Integer siteId = 1;

    @Test
    void getSitesByName() {
        List<SiteDTO> resp = client.exchange(
                String.format("/api/v1/database/sites/name?name=%s", "name"),
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<SiteDTO>>() {}).getBody();
        assertEquals(1, resp.size());
    }

    @Test
    void getSitesByLatLng() {
        List<SiteDTO> resp = client.exchange(
                String.format("/api/v1/database/sites/latlng?lat=%s&lng=%s", "5.1", "2.1"),
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<SiteDTO>>() {}).getBody();
        assertEquals(1, resp.size());
    }

    @Test
    void getSiteById() {
        SiteDTO site = client.getForObject("/api/v1/database/sites/" + siteId, SiteDTO.class);
        assertEquals(1, site.getId());
    }

    @Test
    @Sql({"/site-data.sql"})
    void addSite() throws Exception {
        SiteRequest req = new SiteRequest(
                "new site",
                5.0,
                1.0
        );
        String resp = this.mvc.perform(post("/api/v1/database/sites")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(req)))
                .andReturn().getResponse().getContentAsString();

        assertTrue(resp.contains("\"id\":1"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
