package org.zerock.voteservice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.dto.user.login.UserLoginRequestDto;
import org.zerock.voteservice.security.jwt.JwtUtil;

import java.io.IOException;

@Log4j2
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public UserAuthenticationFilter(ObjectMapper objectMapper, JwtUtil jwtUtil) {
        super();
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        log.info("------------UserAuthenticationFilter------------");

        UserLoginRequestDto dto;

        try {
            dto = this.objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);
        } catch (IOException e) {
            log.error("Error parsing JSON authentication request", e);
            throw new AuthenticationServiceException("Error parsing authentication request JSON", e);
        }

        log.info("username: ${}, password: ${}", dto.getUsername(), dto.getPassword());

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult
    ) {
        log.info("------------UserAuthenticationFilter::successfulAuthentication------------");

        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authResult.getPrincipal();

        String username = userDetails.getUsername();
        String role = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("사용자에게 할당된 권한이 없습니다."));

        String token = jwtUtil.createJwt(username, role);

        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed
    ) {
        log.info("------------UserAuthenticationFilter::unsuccessfulAuthentication------------");
        response.setStatus(404);
    }
}
