package org.zerock.voteservice;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class VoteServiceApplicationTests {

    @BeforeAll
    static void setupEnvironmentVariables() {
        try {
            Dotenv dotenv = Dotenv.configure().load();

            log.info("--- Loading Environment Variables for Test Environment ---");

            dotenv.entries().forEach((entry) -> {
                System.setProperty(entry.getKey(), entry.getValue());
                log.info("  -> {} = {}", entry.getKey(), entry.getValue());
            });

            log.info("--- Environment Variable Loading Complete ---");
            log.info("Dotenv variables successfully loaded for tests.");
        } catch (Exception e) {
            log.warn("Failed to load .env file for tests: {}", e.getMessage());
        }
    }

}
