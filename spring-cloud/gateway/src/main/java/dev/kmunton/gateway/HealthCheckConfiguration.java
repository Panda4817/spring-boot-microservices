package dev.kmunton.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class HealthCheckConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(HealthCheckConfiguration.class);


    private RestTemplate restTemplate;

    @Autowired
    public HealthCheckConfiguration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    HealthContributor healthCheckMicroservices(){
        final Map<String, HealthIndicator> registry = new LinkedHashMap<>();
        registry.put("geocode", () -> getHealth("http://geocode"));
        registry.put("joke", () -> getHealth("http://joke"));
        registry.put("restaurant-thirdparty", () -> getHealth("http://restaurant-thirdparty"));
        registry.put("site", () -> getHealth("http://site"));
        registry.put("restaurant", () -> getHealth("http://restaurant"));
        registry.put("review", () -> getHealth("http://review"));
        registry.put("restaurant-composite", () -> getHealth("http://restaurant-composite"));
        registry.put("review-composite", () -> getHealth("http://review-composite"));
        return CompositeHealthContributor.fromMap(registry);
    }

    private Health getHealth(String baseUrl) {
        String url = baseUrl + "/actuator/health";
        LOG.debug("Will call the Health API on URL: {}", url);
        HttpStatus status = restTemplate.getForEntity(url, Object.class).getStatusCode();
        if (status.value() == 200) {
            return Health.up().build();
        } else {
            LOG.info(status.toString());
            return Health.down().withDetail("error code", status.value()).build();
        }
    }
}
