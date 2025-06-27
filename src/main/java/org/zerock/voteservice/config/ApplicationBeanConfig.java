package org.zerock.voteservice.config;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class ApplicationBeanConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
