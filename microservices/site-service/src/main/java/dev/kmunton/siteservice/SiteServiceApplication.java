package dev.kmunton.siteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.kmunton")
@EntityScan("dev.kmunton.entities")
public class SiteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiteServiceApplication.class, args);
	}

}
