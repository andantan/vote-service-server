package org.zerock.voteservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.zerock.voteservice.security.filter.JwtAuthenticationFilter;
import org.zerock.voteservice.security.jwt.JwtUtil;
import org.zerock.voteservice.security.filter.UserAuthenticationFilter;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;
import org.zerock.voteservice.adapter.in.web.controller.user.login.service.UserLoginService;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.username.parameter.property}")
    private String usernameParameterProperty;

    @Value("${spring.security.password.parameter.property}")
    private String passwordParameterProperty;

    private final PublicEndpointsProperties publicEndpointsProperties;
    private final UserLoginService userLoginService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public SecurityConfig(
            PublicEndpointsProperties publicEndpointsProperties,
            UserLoginService userLoginService,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper,
            JwtUtil jwtUtil
    ) {
        this.publicEndpointsProperties = publicEndpointsProperties;
        this.userLoginService = userLoginService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = getAuthenticationManager(httpSecurity);

        this.customizeAuthenticationManager(httpSecurity, authenticationManager);
        this.customizeRequestAuthorization(httpSecurity);
        this.customizeSecurityFilterChain(httpSecurity, authenticationManager);

        return httpSecurity.build();
    }

    private void customizeAuthenticationManager(
            HttpSecurity httpSecurity, AuthenticationManager authenticationManager
    ) {
        httpSecurity.authenticationManager(authenticationManager);
    }

    private void customizeRequestAuthorization(
            HttpSecurity httpSecurity
    ) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(publicEndpointsProperties.getPermittedRequestMatchers()).permitAll()
                .anyRequest().authenticated());
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void customizeSecurityFilterChain(
            HttpSecurity httpSecurity, AuthenticationManager authenticationManager
    ) {
        httpSecurity.addFilterBefore(
                this.createJwtAuthenticationFilter(),
                UserAuthenticationFilter.class
        );
        httpSecurity.addFilterAt(
                this.createUserAuthenticationFilter(authenticationManager),
                UsernamePasswordAuthenticationFilter.class
        );
    }

    private AuthenticationManager getAuthenticationManager(
            HttpSecurity httpSecurity
    ) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userLoginService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    private UserAuthenticationFilter createUserAuthenticationFilter(
            AuthenticationManager authenticationManager
    ) {
        UserAuthenticationFilter filter = new UserAuthenticationFilter(objectMapper, jwtUtil);

        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl(publicEndpointsProperties.getLoginEndpoint());
        filter.setUsernameParameter(usernameParameterProperty);
        filter.setPasswordParameter(passwordParameterProperty);

        return filter;
    }

    private JwtAuthenticationFilter createJwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }
}