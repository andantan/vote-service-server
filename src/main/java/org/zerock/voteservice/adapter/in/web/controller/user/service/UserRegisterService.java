package org.zerock.voteservice.adapter.in.web.controller.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.dto.user.register.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.adapter.out.persistence.repository.UserRepository;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(UserRegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername()) || userRepository.existsByEmail(dto.getEmail())) {
            return;
        }

        userRepository.save(UserEntity.newUserEntity(dto, passwordEncoder));
    }
}
