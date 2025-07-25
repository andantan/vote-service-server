package org.zerock.voteservice.config;

import com.fasterxml.jackson.core.JsonParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
@AllArgsConstructor
public class ApplicationBeanConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        return objectMapper;
    }
}
