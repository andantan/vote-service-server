package org.zerock.voteservice.security.jwt;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import org.zerock.voteservice.tool.time.DateUtil;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final Long expireMinutes;

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

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(DateUtil.now());
    }

    public String createJwt(String username, String role) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(DateUtil.now())
                .expiration(DateUtil.after(expireMinutes))
                .signWith(this.secretKey)
                .compact();
    }
}
