package org.zerock.voteservice.adapter.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.voteservice.adapter.out.jpa.entity.UserVerificationEntity;

import java.util.Date;
import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerificationEntity, String> {
    Optional<UserVerificationEntity> findByUsername(String username);

    void deleteByUsername(String username);

    @Transactional
    @Modifying
    int deleteByExpirationBefore(Date now);
}
