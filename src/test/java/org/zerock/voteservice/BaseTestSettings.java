package org.zerock.voteservice;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public abstract class BaseTestSettings {
    @BeforeAll
    static void setupEnvironmentVariables() {
        Dotenv dotenv = Dotenv.configure().load();

        dotenv.entries()
                .forEach((entry) -> System.setProperty(entry.getKey(), entry.getValue()));

    }
}
