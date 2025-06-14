package org.zerock.voteservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
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
                reactWebServerPort);

        registry.addMapping("/**")
                .allowedOrigins(reactClientOrigin)
                .allowedMethods("GET", "POST")
                .allowCredentials(true)
                .maxAge(3600);
    }
}