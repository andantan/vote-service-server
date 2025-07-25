package org.zerock.voteservice.adapter.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.voteservice.adapter.out.jpa.entity.UserRefreshTokenRotateEntity;

import java.util.Optional;

public interface UserRefreshTokenRotateRepository extends JpaRepository<UserRefreshTokenRotateEntity, Long> {
    Optional<UserRefreshTokenRotateEntity> findByUid(Long uid);
}
