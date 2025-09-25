package org.zerock.voteservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.web.cors.CorsConfigurationSource;
import org.zerock.voteservice.security.filter.*;
import org.zerock.voteservice.security.user.UserAuthenticationService;
import org.zerock.voteservice.security.handler.*;
import org.zerock.voteservice.security.jwt.JwtUtil;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;
import org.zerock.voteservice.security.user.UserRefreshTokenRotateService;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${spring.security.username.parameter.property}")
    private String usernameParameterProperty;

    @Value("${spring.security.password.parameter.property}")
    private String passwordParameterProperty;

    private final UserAuthenticationErrorHandler userAuthenticationErrorHandler;
    private final UserRefreshTokenRotateService userRefreshTokenRotateService;
    private final UserAuthenticationService userAuthenticationService;
    private final PublicEndpointsProperties publicEndpointsProperties;
    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity,
            AuthenticationManager authenticationManager
    ) throws Exception {
        this.customizeAuthenticationManager(httpSecurity, authenticationManager);
        this.customizeRequestAuthorization(httpSecurity);
        this.customExceptionHandling(httpSecurity);
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
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        // httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(publicEndpointsProperties.getPermittedRequestMatchers()).permitAll()
                .anyRequest().authenticated());
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void customExceptionHandling(
            HttpSecurity httpSecurity
    ) throws Exception {
        AuthenticationEntryPoint authEntryPoint = new UserAuthenticationEntryPoint(userAuthenticationErrorHandler);
        AccessDeniedHandler accessDeniedHandler = new UserAccessDeniedHandler(objectMapper);

        httpSecurity.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );
    }

    private void customizeSecurityFilterChain(
            HttpSecurity httpSecurity, AuthenticationManager authenticationManager
    ) {
        httpSecurity.addFilterAt(
                this.createUserAuthenticationFilter(authenticationManager),
                UsernamePasswordAuthenticationFilter.class
        );
        httpSecurity.addFilterBefore(
                this.createJwtAccessAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
        );
        httpSecurity.addFilterBefore(
                this.createJwtRefreshAuthenticationFilter(),
                JwtAccessAuthenticationFilter.class
        );
        httpSecurity.addFilterBefore(
                this.createJwtValidationFilter(),
                JwtRefreshAuthenticationFilter.class
        );
        httpSecurity.addFilterAfter(
                this.createUserHashValidationFilter(),
                JwtAccessAuthenticationFilter.class
        );
        httpSecurity.addFilterAfter(
                this.createUserLogoutFilter(),
                UserHashValidationFilter.class
        );
    }

    private UserAuthenticationFilter createUserAuthenticationFilter(
            AuthenticationManager authenticationManager
    ) {
        UserAuthenticationFilter filter = new UserAuthenticationFilter(objectMapper);

        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl(publicEndpointsProperties.getLoginEndpoint());
        filter.setUsernameParameter(usernameParameterProperty);
        filter.setPasswordParameter(passwordParameterProperty);

        UserAuthenticationSuccessHandler successHandler = new UserAuthenticationSuccessHandler(
                userRefreshTokenRotateService, objectMapper, jwtUtil
        );
        UserAuthenticationFailureHandler failureHandler = new UserAuthenticationFailureHandler(objectMapper);

        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

        return filter;
    }

    private UserLogoutFilter createUserLogoutFilter() {
        return new UserLogoutFilter(
                userRefreshTokenRotateService, publicEndpointsProperties, objectMapper, jwtUtil
        );
    }

    private JwtValidationFilter createJwtValidationFilter() {
        return new JwtValidationFilter(jwtUtil, publicEndpointsProperties);
    }

    private JwtRefreshAuthenticationFilter createJwtRefreshAuthenticationFilter() {
        return new JwtRefreshAuthenticationFilter(
                jwtUtil, objectMapper, publicEndpointsProperties, userRefreshTokenRotateService
        );
    }

    private JwtAccessAuthenticationFilter createJwtAccessAuthenticationFilter() {
        return new JwtAccessAuthenticationFilter(jwtUtil, publicEndpointsProperties);
    }

    private UserHashValidationFilter createUserHashValidationFilter() {
        return new UserHashValidationFilter(
                userAuthenticationService, publicEndpointsProperties
        );
    }
}