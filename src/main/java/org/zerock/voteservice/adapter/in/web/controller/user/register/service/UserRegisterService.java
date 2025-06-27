package org.zerock.voteservice.adapter.in.web.controller.user.register.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.dto.user.error.UserErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.user.error.status.UserRegisterErrorStatus;
import org.zerock.voteservice.adapter.in.web.dto.user.register.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.adapter.out.persistence.repository.UserRepository;
import org.zerock.voteservice.tool.hash.Sha256;

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

    public UserRegisterServiceResult validateUserExistence(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            return UserRegisterServiceResult.failure("EXIST_USERNAME");
        }

        if (userRepository.existsByEmail(email)) {
            return UserRegisterServiceResult.failure("EXIST_EMAIL");
        }

        return UserRegisterServiceResult.successWithoutData();
    }

    public UserRegisterServiceResult register(UserRegisterRequestDto dto) {
        UserEntity newUserEntity;

        try {
            newUserEntity = UserEntity.newUserEntity(dto, passwordEncoder);
        } catch (IllegalArgumentException e) {
            return UserRegisterServiceResult.failureWithMessage("INVALID_PARAMETER", e.getMessage());
        }

        UserEntity savedUserEntity = userRepository.save(newUserEntity);

        // TODO: userHash
        String userHash = Sha256.generateUserHash(savedUserEntity.getId(), dto);

        return UserRegisterServiceResult.success("OK", savedUserEntity.getId());
    }

    public ResponseEntity<UserErrorResponseDto> getErrorResponse(UserRegisterServiceResult result) {
        UserRegisterErrorStatus errorStatus = UserRegisterErrorStatus.fromCode(result.getStatus());
        UserErrorResponseDto errorDto;

        if (!result.isExistMessage()) {
            errorDto = UserErrorResponseDto.from(errorStatus);
        } else {
            errorDto = UserErrorResponseDto.fromWithCustomMessage(errorStatus, result.getMessage());
        }

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }
}
