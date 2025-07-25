package org.zerock.voteservice.adapter.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByUid(Long uid);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUidAndUsername(Long uid, String username);

    void deleteByUid(Long uid);
}
