package org.zerock.voteservice.security.jwt;

import io.jsonwebtoken.Claims;
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
    private final SecretKey secretKey;
    @Getter private final Long expireMinutes;

    public JwtUtil(
            @Value("${spring.security.jwt.secret.key}") String secretKey,
            @Value("${spring.security.jwt.expire.delta}") Long expireMinutes
    ) {
        this.secretKey = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.expireMinutes = expireMinutes;
    }

    public String createJwt(Integer uid, String userHash, String username, String role) {
        return Jwts.builder()
                .claim("uid", uid)
                .claim("user_hash", userHash)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(DateUtil.now())
                .expiration(DateUtil.after(expireMinutes))
                .signWith(this.secretKey)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getUsername(Claims claims) {
        return claims.get("username", String.class);
    }

    public String getRole(Claims claims) {
        return claims.get("role", String.class);
    }

    public String getUserHash(Claims claims) {
        return claims.get("user_hash", String.class);
    }

    public Integer getUid(Claims claims) {
        return claims.get("uid", Integer.class);
    }

    public Boolean validateEssentialClaims(Claims claims) {
        if (claims == null) {
            return false;
        }

        return getUid(claims) != null
                && getUserHash(claims) != null
                && getRole(claims) != null
                && getUsername(claims) != null;
    }
}
