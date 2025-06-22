package org.zerock.voteservice.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String apiTitle = "블록체인 기반 전자투표 서비스 API";
        String apiDescription = "블록체인 기술을 활용한 전자투표 서비스의 RESTful API 문서";
        String apiVersion = "0.1.0";

        Info apiInfo = new Info()
                .title(apiTitle)
                .description(apiDescription)
                .version(apiVersion);

        return new OpenAPI()
                .info(apiInfo);
    }
}
