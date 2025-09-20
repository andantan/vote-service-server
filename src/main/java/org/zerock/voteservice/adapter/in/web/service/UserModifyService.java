package org.zerock.voteservice.adapter.in.web.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.CommonFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserPasswordModifyRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserModifyPasswordSuccessResponseDto;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.adapter.out.jpa.repository.UserRepository;

import java.util.Optional;

@Service
@Log4j2
public class UserModifyService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserModifyService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserModifyServiceResult modifyPassword(UserPasswordModifyRequestDto dto) {
        Optional<UserEntity> optionalUser;

        try {
            optionalUser = this.userRepository.findByUidAndUsername(dto.getUid(), dto.getUsername());
        } catch (Exception e) {
            log.error("Failed to save user to MariaDB: {}", dto.getUsername(), e);
            return UserModifyServiceResult.failure(UserModifyServiceStatus.INTERNAL_SERVER_ERROR);
        }

        if (optionalUser.isEmpty()) {
            return UserModifyServiceResult.failure(UserModifyServiceStatus.USER_NOT_EXIST);
        }

        UserEntity user = optionalUser.get();

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        UserEntity modifiedUser;
        try {
            modifiedUser =  this.userRepository.save(user);
        } catch (Exception e) {
            log.error("Failed to save user to MariaDB: {}", dto.getUsername(), e);
            return UserModifyServiceResult.failure(UserModifyServiceStatus.INTERNAL_SERVER_ERROR);
        }

        return UserModifyServiceResult.success(modifiedUser);
    }

    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(UserModifyServiceResult result) {
        UserModifyServiceStatus successStatus = result.getStatus();

        UserModifyPasswordSuccessResponseDto responseDto = UserModifyPasswordSuccessResponseDto.builder()
                .success(true)
                .status(successStatus.getCode())
                .message(successStatus.getMessage())
                .httpStatusCode(successStatus.getHttpStatusCode().value())
                .uid(result.getUid())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getHttpStatusCode()));
    }

    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(UserModifyServiceResult result) {
        UserModifyServiceStatus failureStatus = result.getStatus();

        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .status(failureStatus.getCode())
                .message(failureStatus.getMessage())
                .httpStatusCode(failureStatus.getHttpStatusCode().value())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureStatus.getHttpStatusCode().value()));
    }

    public ResponseEntity<? extends ResponseDto> getAbnormalResponseEntity() {
        UserModifyServiceStatus failureStatus = UserModifyServiceStatus.ABNORMAL_REQUEST;

        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .status(failureStatus.getCode())
                .message(failureStatus.getMessage())
                .httpStatusCode(failureStatus.getHttpStatusCode().value())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
