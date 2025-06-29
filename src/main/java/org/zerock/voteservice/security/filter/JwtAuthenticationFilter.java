package org.zerock.voteservice.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.security.jwt.JwtUtil;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;

import java.io.IOException;
import java.util.List;

@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PublicEndpointsProperties publicEndpointsProperties;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            PublicEndpointsProperties publicEndpointsProperties
    ) {
        this.jwtUtil = jwtUtil;
        this.publicEndpointsProperties = publicEndpointsProperties;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("------------JwtAuthenticationFilter::doFilterInternal------------");

        String requestUri = request.getRequestURI();
        String authorizationHeader = request.getHeader("Authorization");

        if (!this.isValidAuthorizationHeader(authorizationHeader)) {
            log.warn("JWT token is missing or does not start with 'Bearer '. (Path: {})", requestUri);
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "인증 토큰이 필요합니다. (형식: Bearer <token>)"
            );
            return;
        }

        String token = this.extractJwtToken(authorizationHeader);

        Claims claims;
        Integer uid;
        String userHash;
        String username;
        String role;

        try {
            claims = jwtUtil.extractAllClaims(token);

            if (!this.jwtUtil.validateEssentialClaims(claims)) {
                log.warn("Essential claims are missing or invalid in JWT token. (Path: {})", requestUri);

                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "토큰이 유효하지 않거나 손상되었습니다: 필수 클레임이 누락되었습니다."
                );

                return;
            }

            username = jwtUtil.getUsername(claims);
            role = jwtUtil.getRole(claims);
            userHash = jwtUtil.getUserHash(claims);
            uid = jwtUtil.getUid(claims);

        } catch (io.jsonwebtoken.security.SignatureException | io.jsonwebtoken.MalformedJwtException e) {
            log.error("Invalid JWT signature or malformed token (Path: {})", requestUri);

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "유효하지 않은 인증 토큰입니다: 서명이 유효하지 않거나 토큰 형식이 잘못되었습니다."
            );

            return;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("Expired JWT token caught: {} (Path: {})", e.getMessage(), requestUri);

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "토큰이 만료되었습니다."
            );

            return;
        } catch (Exception e) {
            log.error("Unexpected error during JWT processing: {} (Path: {})", e.getMessage(), requestUri);

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "인증 토큰 유효성 검사 중 예상치 못한 오류가 발생했습니다."
            );

            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userEntity;

            try {
                userEntity = UserEntity.newJwtUserEntity(uid, username, role);
            } catch (IllegalArgumentException e) {
                log.error("Invalid role value in JWT: {} (Path: {})", role, requestUri);

                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "토큰에 유효하지 않은 사용자 역할이 포함되어 있습니다."
                );

                return;
            }

            UserAuthenticationDetails userAuthenticationDetails = new UserAuthenticationDetails(userEntity, userHash);
            log.info("JWT Details user hash: {}", userAuthenticationDetails.getUserHash());

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userAuthenticationDetails,
                    null,
                    userAuthenticationDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("JWT authentication successful: Uid '{}' Username '{}', Role '{}', UserHash '{}'",
                    uid, username, role, userHash);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludedPaths = publicEndpointsProperties.getExcludedJwtAuthenticationEndpoints();
        String requestUri = request.getRequestURI();

        return excludedPaths.contains(requestUri) || requestUri.startsWith("/documents");
    }

    private boolean isValidAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String extractJwtToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }
}
