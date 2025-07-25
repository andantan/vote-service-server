package org.zerock.voteservice.security.user;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.voteservice.adapter.out.jpa.entity.UserRefreshTokenRotateEntity;
import org.zerock.voteservice.adapter.out.jpa.repository.UserRefreshTokenRotateRepository;
import org.zerock.voteservice.security.jwt.JwtUtil;

import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class UserRefreshTokenRotateService {
    private final JwtUtil jwtUtil;
    private final UserRefreshTokenRotateRepository userRefreshTokenRotateRepository;

    @Transactional
    public void addRefreshToken(String refreshToken) {
        Claims refreshClaims = jwtUtil.extractAllClaims(refreshToken);

        UserRefreshTokenRotateEntity entity = UserRefreshTokenRotateEntity.builder()
                .uid(jwtUtil.getUid(refreshClaims))
                .username(jwtUtil.getUsername(refreshClaims))
                .expiration(jwtUtil.getExpiration(refreshClaims))
                .refresh(refreshToken)
                .build();

        log.info("AddedEntity -> uid: {}", entity.getUid());
        log.info("AddedEntity -> username: {}", entity.getUsername());
        log.info("AddedEntity -> expiration: {}", entity.getExpiration());
        log.info("AddedEntity -> refresh: {}", entity.getRefresh());

        userRefreshTokenRotateRepository.save(entity);
    }

    @Transactional
    public void updateRefreshToken(String newRefreshToken) {
        Claims refreshClaims = jwtUtil.extractAllClaims(newRefreshToken);

        UserRefreshTokenRotateEntity entity = UserRefreshTokenRotateEntity.builder()
                .uid(jwtUtil.getUid(refreshClaims))
                .username(jwtUtil.getUsername(refreshClaims))
                .expiration(jwtUtil.getExpiration(refreshClaims))
                .refresh(newRefreshToken)
                .build();

        log.info("updatedEntity -> uid: {}", entity.getUid());
        log.info("updatedEntity -> username: {}", entity.getUsername());
        log.info("updatedEntity -> expiration: {}", entity.getExpiration());
        log.info("updatedEntity -> refresh: {}", entity.getRefresh());


        userRefreshTokenRotateRepository.save(entity);
    }

    public Boolean validateRefreshToken(String refreshToken) {
        Claims claims = jwtUtil.extractAllClaims(refreshToken);

        Optional<UserRefreshTokenRotateEntity> opt = userRefreshTokenRotateRepository.findByUid(jwtUtil.getUid(claims));

        if (opt.isEmpty()) {
            return false;
        }

        UserRefreshTokenRotateEntity entity = opt.get();

        if (entity.getExpiration() == null || entity.getRefresh() == null) {
            return false;
        }

        return entity.getUid().equals(jwtUtil.getUid(claims))
                && entity.getUsername().equals(jwtUtil.getUsername(claims))
                && entity.getExpiration().equals(jwtUtil.getExpiration(claims))
                && entity.getRefresh().equals(refreshToken);
    }
}
