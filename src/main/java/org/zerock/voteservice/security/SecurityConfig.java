package org.zerock.voteservice.security;

import lombok.extern.log4j.Log4j2;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.zerock.voteservice.security.jwt.LoginFilter;
import org.zerock.voteservice.security.property.AuthorizationEndpointProperties;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthorizationEndpointProperties authorizationEndpointProperties;

    public SecurityConfig(
            AuthorizationEndpointProperties authorizationEndpointProperties
    ) {
        this.authorizationEndpointProperties = authorizationEndpointProperties;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public LoginFilter loginFilter(
            AuthenticationManager authenticationManager, ObjectMapper objectMapper
    ) {
        LoginFilter filter = new LoginFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl("/api/v1/user/login");
        filter.setUsernameParameter("username");
        filter.setPasswordParameter("password");
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        this.customizeRequestAuthorization(httpSecurity);
        this.securityCustomizeFilterChain(httpSecurity);

        return httpSecurity.build();
    }

    private void customizeRequestAuthorization(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(authorizationEndpointProperties.getPermittedRequestMatchers()).permitAll()
                .anyRequest().authenticated());
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void securityCustomizeFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterAt(loginFilter(
                authenticationManager(httpSecurity.getSharedObject(AuthenticationConfiguration.class)),
                objectMapper()
        ), UsernamePasswordAuthenticationFilter.class);
    }
}
