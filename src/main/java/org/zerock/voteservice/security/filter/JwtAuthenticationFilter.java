package org.zerock.voteservice.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.dto.user.role.UserRole;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.security.jwt.JwtUtil;

import java.io.IOException;

@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("------------JwtAuthenticationFilter::doFilterInternal------------");

        String authorizationHeader = request.getHeader("Authorization");

        if (!this.isValidAuthorizationHeader(authorizationHeader)) {
            log.warn("Token is not valid");

            filterChain.doFilter(request, response);
            return;
        }

        String token = this.extractJwtToken(authorizationHeader);

        if (this.jwtUtil.isExpired(token)) {
            log.warn("Token({}) is expired", token);

            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        String enumRole = role.startsWith("ROLE_") ?
                role.substring("ROLE_".length()) :
                role;

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("temp");
        userEntity.setRole(UserRole.valueOf(enumRole));

        UserAuthenticationDetails userAuthenticationDetails = new UserAuthenticationDetails(userEntity);
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userAuthenticationDetails, null, userAuthenticationDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String extractJwtToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }
}
