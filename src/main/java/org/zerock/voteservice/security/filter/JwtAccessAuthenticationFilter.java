package org.zerock.voteservice.security.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.voteservice.security.jwt.JwtUtil;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;
import org.zerock.voteservice.tool.http.HttpHelper;

import java.io.IOException;
import java.util.List;

@Log4j2
public class JwtAccessAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PublicEndpointsProperties publicEndpointsProperties;

    public JwtAccessAuthenticationFilter(
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
        HttpHelper httpHelper = new HttpHelper(request, response);
        Claims claims = jwtUtil.extractAllClaims(httpHelper.getAccessToken());
        String logPrefix = String.format("[UID:%d] ", jwtUtil.getUid(claims));

        try {
            httpHelper.setAuthenticationContext(
                    jwtUtil.getUid(claims),
                    jwtUtil.getUsername(claims),
                    jwtUtil.getUserHash(claims),
                    jwtUtil.getRole(claims)
            );
        } catch (IllegalArgumentException e) {
            log.error("{}Invalid role value in access JWT: {} (Path: {})",
                    logPrefix, jwtUtil.getRole(claims), httpHelper.getUri()
            );
            httpHelper.writeForbiddenError("토큰에 유효하지 않은 사용자 역할이 포함되어 있습니다.");

            return;
        }

        log.debug("{}JWT access authentication successful", logPrefix);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<String> excludedPaths = publicEndpointsProperties.getPermittedEndpoints();
        String requestUri = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();

        for (String pattern : excludedPaths) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }

        return false;
    }
}
