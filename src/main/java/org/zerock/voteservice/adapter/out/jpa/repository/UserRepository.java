package org.zerock.voteservice.adapter.out.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByUid(Integer uid);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUidAndUsername(Integer uid, String username);

    void deleteByUid(Integer uid);
}
