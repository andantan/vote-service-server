package org.zerock.voteservice.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUidAndUsername(Integer uid, String username);
}
