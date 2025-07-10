package org.zerock.voteservice.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.zerock.voteservice.tool.time.DateUtil;

@Component
public class JwtUtil {
    private final static String ACCESS_TOKEN_CATEGORY = "access";
    private final static String REFRESH_TOKEN_CATEGORY = "refresh";
    private final SecretKey secretKey;
    @Getter private final Integer accessJwtExpireSeconds;
    @Getter private final Integer refreshJwtExpireSeconds;

    public JwtUtil(
            @Value("${spring.security.jwt.secret.key}") String secretKey,
            @Value("${spring.security.jwt.access.expire.delta}") Integer accessJwtExpireSeconds,
            @Value("${spring.security.jwt.refresh.expire.delta}") Integer refreshJwtExpireSeconds
    ) {
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
            case ACCESS_TOKEN_CATEGORY -> this.accessJwtExpireSeconds;
            case REFRESH_TOKEN_CATEGORY -> this.refreshJwtExpireSeconds;
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
        return this.createJwtToken(ACCESS_TOKEN_CATEGORY, uid, userHash, username, role);
    }

    public String createRefreshJwt(Integer uid, String userHash, String username, String role) {
        return this.createJwtToken(REFRESH_TOKEN_CATEGORY, uid, userHash, username, role);
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

    public Boolean isExpiredToken(String token) {
        try {
            extractAllClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean isAccessToken(Claims claims) {
        return this.getCategory(claims).equals(ACCESS_TOKEN_CATEGORY);
    }

    public Boolean isRefreshToken(Claims claims) {
        return this.getCategory(claims).equals(REFRESH_TOKEN_CATEGORY);
    }
}
