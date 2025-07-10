package org.zerock.voteservice.tool.http;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class HttpHelper {

    private final static String ACCESS_TOKEN_HEADER_NAME = "Authorization";
    private final static String REFRESH_TOKEN_COOKIE_NAME = "vs-refresh-token";

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public static Cookie createHttpOnlyCookie(String key, String value, Integer maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
//        cookie.setSecure(true);
        return cookie;
    }

    public static Cookie createRefreshTokenCookie(String token, Integer maxAge) {
        return HttpHelper.createHttpOnlyCookie(REFRESH_TOKEN_COOKIE_NAME, token, maxAge);
    }

    public static String getRefreshTokenCookieName() {
        return REFRESH_TOKEN_COOKIE_NAME;
    }

    public String getUri() {
        return request.getRequestURI();
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public String getAuthorizationHeader() {
        return this.getHeader(ACCESS_TOKEN_HEADER_NAME);
    }

    public String getAccessToken() {
        return this.getAuthorizationHeader().substring(7);
    }

    private Optional<Cookie> getCookie(String cookieName) {
        return Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst();
    }

    public String getRefreshToken() {
        Optional<Cookie> refreshTokenCookieOpt = this.getCookie(HttpHelper.REFRESH_TOKEN_COOKIE_NAME);

        if (refreshTokenCookieOpt.isEmpty()) {
            throw new BadCredentialsException("리프레시 토큰 쿠키를 찾을 수 없습니다.");
        }

        return refreshTokenCookieOpt.get().getValue();
    }

    public void setAuthenticationContext(
            Integer uid, String username, String userHash, String role
    ) throws IllegalArgumentException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserEntity userEntity = UserEntity.newJwtUserEntity(uid, username, role);
            UserAuthenticationDetails userAuthenticationDetails = new UserAuthenticationDetails(userEntity, userHash);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userAuthenticationDetails,
                    null,
                    userAuthenticationDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    public boolean isMissingAccessToken() {
        String authHeader = this.getAuthorizationHeader();

        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    public boolean isMissingRefreshToken(String cookieName) {
        return this.getCookie(cookieName).isEmpty();
    }

    public void writeError(int code, String message) throws IOException {
        response.sendError(code, message);
    }

    public void writeForbiddenError(String message) throws IOException {
        this.writeError(HttpServletResponse.SC_FORBIDDEN, message);
    }

    public void writeUnauthorizedError(String message) throws IOException {
        this.writeError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
