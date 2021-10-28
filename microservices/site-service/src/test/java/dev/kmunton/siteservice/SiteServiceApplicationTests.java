package dev.kmunton.siteservice;

import dev.kmunton.testdatabase.PostgreSqlTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class SiteServiceApplicationTests extends PostgreSqlTestBase {

	@Test
	void contextLoads() {
	}

}
