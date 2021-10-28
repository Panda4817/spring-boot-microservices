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
        String urlLiveness = baseUrl + "/actuator/health/liveness";
        String urlReadiness = baseUrl + "/actuator/health/readiness";
        LOG.debug("Will call the Health Liveness API on URL: {}", urlLiveness);
        HttpStatus statusLiveness = restTemplate.getForEntity(urlLiveness, Object.class).getStatusCode();
        LOG.debug("Will call the Health Readiness API on URL: {}", urlReadiness);
        HttpStatus statusReadiness = restTemplate.getForEntity(urlReadiness, Object.class).getStatusCode();
        if (statusLiveness.value() >= 200 && statusLiveness.value() < 400 && statusReadiness.value() >= 200 && statusReadiness.value() < 400) {
            return Health.up().build();
        } else {
            LOG.info(statusLiveness.toString());
            LOG.info(statusReadiness.toString());
            String error = String.format("Liveness: {} and Readiness: {}", statusLiveness.value(), statusReadiness.value());
            return Health.down().withDetail("error code", error).build();
        }
    }
}
