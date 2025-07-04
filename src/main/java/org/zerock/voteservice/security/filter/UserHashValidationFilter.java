package org.zerock.voteservice.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.voteservice.security.user.UserAuthenticationService;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;
import org.zerock.voteservice.tool.hash.Sha256;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class UserHashValidationFilter extends OncePerRequestFilter {

    private static final String USER_HASH_HEADER = "X-User-Hash";

    private final PublicEndpointsProperties publicEndpointsProperties;
    private final UserAuthenticationService userAuthenticationService;

    public UserHashValidationFilter(
            UserAuthenticationService userAuthenticationService,
            PublicEndpointsProperties publicEndpointsProperties
    ) {
        this.userAuthenticationService = userAuthenticationService;
        this.publicEndpointsProperties = publicEndpointsProperties;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserAuthenticationDetails userDetails)) {

            String errorMessage = "Authentication context invalid after JWT validation.";
            log.error("UserHashValidationFilter: {} Request URI: {}", errorMessage, request.getRequestURI());

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "사용자 인증에 실패했습니다. 다시 로그인해주세요."
            );

            return;
        }

        String logPrefix = String.format("[UID:%d] ", userDetails.getUid());

        log.debug("{}UserHashValidationFilter activated for: {}", logPrefix, request.getRequestURI());

        String clientUserHash = request.getHeader(USER_HASH_HEADER);

        if (clientUserHash == null || clientUserHash.isEmpty()) {
            log.warn("{}User hash validation failed: Missing '{}' header in request.",
                    logPrefix, USER_HASH_HEADER);

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "요청 헤더에 사용자 해시가 없습니다."
            );

            return;
        }

        String jwtUserHash = userDetails.getUserHash();

        log.debug("{}Headers(X-User-Hash) Userhash: {}", logPrefix, clientUserHash);
        log.debug("{}Headers(Authorization) Userhash: {}", logPrefix, jwtUserHash);

        if (!clientUserHash.equals(jwtUserHash)) {
            log.warn("{}User hash validation failed: Client hash '{}' does not match JWT hash '{}'.",
                    logPrefix, clientUserHash, jwtUserHash);

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "사용자 해시가 토큰과 일치하지 않습니다. 접근이 거부되었습니다."
            );

            return;
        }

        Optional<UserEntity> jwtUserEntity = this.userAuthenticationService.loadUserByJwt(
                userDetails.getUid(), userDetails.getUsername()
        );

        if (jwtUserEntity.isEmpty()) {
            log.error("{}User hash validation failed: User (UID: {}, Username: {}) not found or active in database, despite valid JWT.",
                    logPrefix, userDetails.getUid(), userDetails.getUsername()
            );

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "사용자 계정 정보가 일치하지 않거나 비활성 상태입니다. 다시 로그인해주세요."
            );

            return;
        }

        String dbCalculatedUserHash = Sha256.sum(jwtUserEntity.get());

        log.debug("{}Database Calculated Userhash: {}", logPrefix, dbCalculatedUserHash);

        if (!clientUserHash.equals(dbCalculatedUserHash)) {
            log.warn("{}User hash validation failed: Client hash '{}' does not match DB calculated hash '{}'. This might indicate a stale token or data tampering.",
                    logPrefix, clientUserHash, dbCalculatedUserHash);

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "사용자 해시가 데이터베이스 계산 결과와 일치하지 않습니다. 접근이 거부되었습니다. 다시 로그인해주세요."
            );

            return;
        }

        log.info("{}UserHash validation successful", logPrefix);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludedPaths = publicEndpointsProperties.getPermittedEndpoints();
        String requestUri = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for (String pattern : excludedPaths) {
            if (pathMatcher.match(pattern, requestUri)) {
                log.debug("Skipping JwtAuthenticationFilter for permitted path: {}", requestUri);
                return true;
            }
        }

        return false;
    }
}
