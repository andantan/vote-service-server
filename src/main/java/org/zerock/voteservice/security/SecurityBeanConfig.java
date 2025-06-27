package org.zerock.voteservice.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
@AllArgsConstructor
public class SecurityBeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
