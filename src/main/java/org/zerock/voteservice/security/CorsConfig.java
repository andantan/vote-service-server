package org.zerock.voteservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Deprecated(since = "2025-08-04")
public class CorsConfig implements WebMvcConfigurer {
    @Value("${react.web.server.protocol}")
    private String reactWebServerProtocol;

    @Value("${react.web.server.host}")
    private String reactWebServerHost;

    @Value("${react.web.server.port}")
    private int reactWebServerPort;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String reactClientOrigin = String.format("%s://%s:%d",
                reactWebServerProtocol,
                reactWebServerHost,
                reactWebServerPort
        );

        registry.addMapping("/**")
                .allowedOrigins(reactClientOrigin)
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("authorization")
                .maxAge(3600);
    }
}