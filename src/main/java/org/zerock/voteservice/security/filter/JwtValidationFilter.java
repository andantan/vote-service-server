package org.zerock.voteservice.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.voteservice.security.jwt.JwtUtil;
import org.zerock.voteservice.security.property.PublicEndpointsProperties;
import org.zerock.voteservice.tool.http.HttpHelper;

import java.io.IOException;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PublicEndpointsProperties publicEndpointsProperties;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        HttpHelper httpHelper = new HttpHelper(request, response);
        String requestUri = httpHelper.getUri();

        if (httpHelper.isMissingAccessToken()) {
            log.warn("JWT token is missing or does not start with 'Bearer '. (Path: {})", httpHelper.getUri());
            httpHelper.writeForbiddenError("인증 토큰이 필요합니다. (형식: Bearer <token>)");
            return;
        }

        try {
            Claims accessTokenClaims = jwtUtil.extractAllClaims(httpHelper.getAccessToken());

            if (!this.jwtUtil.validateEssentialClaims(accessTokenClaims) || !this.jwtUtil.isAccessToken(accessTokenClaims)) {
                log.warn("Essential claims are missing or invalid in JWT token. (Path: {})", requestUri);
                httpHelper.writeForbiddenError("인증 토큰이 유효하지 않습니다.");
                return;
            }

        } catch (ExpiredJwtException e) {
            String logPrefix = String.format("[UID:%d] ", jwtUtil.getUid(e.getClaims()));
            log.debug("{}Access JWT expired for request to {}", logPrefix, requestUri);

        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.debug("Invalid access JWT signature or malformed token (Path: {})", requestUri);
            httpHelper.writeForbiddenError("유효하지 않은 인증 토큰입니다: 서명이 유효하지 않거나 토큰 형식이 잘못되었습니다.");
            return;

        } catch (Exception e) {
            log.error("Unexpected error during access JWT processing: {} (Path: {})", e.getMessage(), requestUri);
            httpHelper.writeForbiddenError("인증 토큰 유효성 검사 중 예상치 못한 오류가 발생했습니다.");
            return;
        }

        String refreshTokenCookieName = HttpHelper.getRefreshTokenCookieName();

        if (httpHelper.isMissingRefreshToken(refreshTokenCookieName)) {
            log.warn("Refresh token cookie not found in the request.");
            httpHelper.writeUnauthorizedError("리프레시 토큰이 필요합니다. 다시 로그인 해주세요.");
            return;
        }

        try {
            Claims refreshTokenClaims = jwtUtil.extractAllClaims(httpHelper.getRefreshToken());

            if (!this.jwtUtil.validateEssentialClaims(refreshTokenClaims) || !this.jwtUtil.isRefreshToken(refreshTokenClaims)) {
                log.warn("Essential claims are missing or invalid in JWT token. (Path: {})", requestUri);
                httpHelper.writeForbiddenError("리프래시 토큰이 유효하지 않습니다. 다시 로그인 해주세요.");

                return;
            }
        } catch (ExpiredJwtException e) {
            log.debug("Refresh JWT expired for request to {}", requestUri);
            httpHelper.writeUnauthorizedError("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요.");

            return;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.debug("Invalid refresh JWT signature or malformed token (Path: {})", requestUri);
            httpHelper.writeForbiddenError("유효하지 않은 리프래시 토큰입니다: 서명이 유효하지 않거나 토큰 형식이 잘못되었습니다.");

            return;
        } catch (Exception e) {
            log.error("Unexpected error during refresh JWT processing: {} (Path: {})", e.getMessage(), requestUri);
            httpHelper.writeForbiddenError("리프래시 토큰 유효성 검사 중 예상치 못한 오류가 발생했습니다.");

            return;
        }

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