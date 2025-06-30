package org.zerock.voteservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
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

        String securitySchemeName = "BearerAuth";
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("JWT 토큰을 입력해주세요.");

        String userHashSchemeName = "X-User-Hash";
        SecurityScheme userHashSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(userHashSchemeName)
                .description("사용자 고유 해시 값 (인증된 사용자의 식별자)");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securitySchemeName)
                .addList(userHashSchemeName);

        return new OpenAPI()
                .info(apiInfo)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, securityScheme)
                        .addSecuritySchemes(userHashSchemeName, userHashSecurityScheme))
                .addSecurityItem(securityRequirement);
    }
}
