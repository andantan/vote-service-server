package org.zerock.voteservice.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    UserEntity findByUsername(String username);
}
