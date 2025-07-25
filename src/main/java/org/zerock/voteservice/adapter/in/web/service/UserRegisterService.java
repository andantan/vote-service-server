package org.zerock.voteservice.adapter.in.web.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.CommonFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.adapter.out.jpa.repository.UserRepository;

@Service
@Log4j2
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

    public UserRegisterServiceResult validateUserExistence(
            String username, String email, String phoneNumber
    ) {
        if (userRepository.existsByUsername(username)) {
            return UserRegisterServiceResult.failure(UserRegisterServiceStatus.EXIST_USERNAME);
        }

        if (userRepository.existsByEmail(email)) {
            return UserRegisterServiceResult.failure(UserRegisterServiceStatus.EXIST_EMAIL);
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            return UserRegisterServiceResult.failure(UserRegisterServiceStatus.EXIST_PHONENUMBER);
        }

        return UserRegisterServiceResult.successWithoutData();
    }

    @Transactional
    public UserRegisterServiceResult register(UserRegisterRequestDto dto) {
        UserEntity newUserEntity;

        try {
            newUserEntity = UserEntity.newUserEntity(dto, passwordEncoder);
        } catch (IllegalArgumentException e) {
            return UserRegisterServiceResult.failure(UserRegisterServiceStatus.INVALID_PARAMETER);
        }

        try {
            UserEntity savedUserEntity = userRepository.save(newUserEntity);

            return UserRegisterServiceResult.success(savedUserEntity);
        } catch (Exception e) {
            log.error("Failed to save user to MariaDB: {}", dto.getUsername(), e);
            return UserRegisterServiceResult.failure(UserRegisterServiceStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void rollbackUserCreation(String attemptedUsername, Long uid) {
        String logPrefix = String.format("[AttemptingRegisterUsername:%s] ", attemptedUsername);

        try {
            log.warn("{}Attempting MariaDB rollback due to gRPC error for user: [UID: {}]", logPrefix, uid);

            if (userRepository.existsByUid(uid)) {
                userRepository.deleteByUid(uid);
            }

            log.warn("{}MariaDB rollback completed due to gRPC error for user: [UID: {}]", logPrefix, uid);
        } catch (Exception e) {
            log.error("{}Failed to rollback MariaDB user entry for UID: {}. Error: {}",
                    logPrefix, uid, e.getMessage(), e);
        }
    }

    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(UserRegisterServiceResult result) {
        UserRegisterServiceStatus failureStatus = result.getStatus();

        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .status(failureStatus.getCode())
                .message(failureStatus.getMessage())
                .httpStatusCode(failureStatus.getHttpStatusCode().value())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
