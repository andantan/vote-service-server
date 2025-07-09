package org.zerock.voteservice.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.zerock.voteservice.tool.time.DateUtil;

@Component
public class JwtUtil {
    private final CookieUtil cookieUtil;
    private final SecretKey secretKey;
    private final Integer accessJwtExpireSeconds;
    private final Integer refreshJwtExpireSeconds;

    public JwtUtil(
            CookieUtil cookieUtil,
            @Value("${spring.security.jwt.secret.key}") String secretKey,
            @Value("${spring.security.jwt.access.expire.delta}") Integer accessJwtExpireSeconds,
            @Value("${spring.security.jwt.refresh.expire.delta}") Integer refreshJwtExpireSeconds
    ) {
        this.cookieUtil = cookieUtil;
        this.secretKey = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.accessJwtExpireSeconds = accessJwtExpireSeconds;
        this.refreshJwtExpireSeconds = refreshJwtExpireSeconds;
    }

    private String createJwtToken(
            String category,
            Integer uid,
            String userHash,
            String username,
            String role
    ) {
        Integer expireDelta = switch (category) {
            case "access" -> this.accessJwtExpireSeconds;
            case "refresh" -> this.refreshJwtExpireSeconds;
            default -> 0;
        };

        return Jwts.builder()
                .claim("category", category)
                .claim("uid", uid)
                .claim("user_hash", userHash)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(DateUtil.now())
                .expiration(DateUtil.after(expireDelta))
                .signWith(this.secretKey)
                .compact();
    }

    public String createAccessJwt(Integer uid, String userHash, String username, String role) {
        return this.createJwtToken("access", uid, userHash, username, role);
    }

    public String createRefreshJwt(Integer uid, String userHash, String username, String role) {
        return this.createJwtToken("refresh", uid, userHash, username, role);
    }

    public Cookie createRefreshJwtCookie(String refreshToken) {
        return this.cookieUtil.createHttpOnlyCookie("refresh", refreshToken, this.accessJwtExpireSeconds);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getCategory(Claims claims) {
        return claims.get("category", String.class);
    }

    public Integer getUid(Claims claims) {
        return claims.get("uid", Integer.class);
    }

    public String getUserHash(Claims claims) {
        return claims.get("user_hash", String.class);
    }

    public String getUsername(Claims claims) {
        return claims.get("username", String.class);
    }

    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

    public Boolean validateEssentialClaims(Claims claims) {
        if (claims == null) {
            return false;
        }

        return getCategory(claims) != null
                && getUid(claims) != null
                && getUserHash(claims) != null
                && getRole(claims) != null
                && getUsername(claims) != null;
    }
}
