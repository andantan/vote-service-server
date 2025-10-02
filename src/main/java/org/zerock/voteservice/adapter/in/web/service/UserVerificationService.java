package org.zerock.voteservice.adapter.in.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.CommonFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserVerificationRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserVerificationSuccessResponseDto;
import org.zerock.voteservice.adapter.out.jpa.entity.UserVerificationEntity;
import org.zerock.voteservice.adapter.out.jpa.repository.UserVerificationRepository;
import org.zerock.voteservice.tool.time.DateUtil;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserVerificationRepository userVerificationRepository;

    @Value("${code.gmail.verification.exp.delta}")
    private int verificationDelta;

    @Transactional
    public UserVerificationResult cacheVerificationCode(UserVerificationRequestDto dto) {
        Optional<UserVerificationEntity> optionalEntity = userVerificationRepository.findByUsername(dto.getUsername());

        UserVerificationEntity verificationEntity;

        if (optionalEntity.isPresent()) {
            verificationEntity = optionalEntity.get();
            verificationEntity.setRealName(dto.getRealname());
            verificationEntity.setEmail(dto.getEmail());
            verificationEntity.setPhoneNumber(dto.getPhoneNumber());
            verificationEntity.setCode(dto.getCode());
            verificationEntity.setExpiration(DateUtil.after(verificationDelta * 60L));
            verificationEntity.setCategory(dto.getCategory());
        } else {
            verificationEntity = UserVerificationEntity.builder()
                    .username(dto.getUsername())
                    .realName(dto.getRealname())
                    .email(dto.getEmail())
                    .phoneNumber(dto.getPhoneNumber())
                    .code(dto.getCode())
                    .expiration(DateUtil.after(verificationDelta * 60L))
                    .category(dto.getCategory())
                    .build();
        }

        if (verificationEntity.getCategory().equals("reset-password")) {
            verificationEntity.setUid(dto.getUid());
        } else {
            verificationEntity.setUid(null);
        }

        try {
            UserVerificationEntity savedEntity = userVerificationRepository.save(verificationEntity);
            return UserVerificationResult.success(savedEntity);
        } catch (Exception e) {
            log.error("Failed to save or update user to MariaDB: {}", dto.getUsername(), e);
            return UserVerificationResult.failure(UserVerificationStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public UserVerificationResult matchVerificationCode(UserVerificationRequestDto dto) {
        Optional<UserVerificationEntity> entityOpt;

        try {
            entityOpt = this.userVerificationRepository.findByUsername(dto.getUsername());
        } catch (Exception e) {
            log.error("Failed to get user from MariaDB: {}", dto.getUsername(), e);
            return UserVerificationResult.failure(UserVerificationStatus.INTERNAL_SERVER_ERROR);
        }

        if (entityOpt.isEmpty()) {
            return UserVerificationResult.failure(UserVerificationStatus.INVALID_CODE);
        }

        UserVerificationEntity userVerificationEntity = entityOpt.get();

        if (!userVerificationEntity.getUsername().equals(dto.getUsername())
            || !userVerificationEntity.getEmail().equals(dto.getEmail())
            || !userVerificationEntity.getRealName().equals(dto.getRealname())
            || !userVerificationEntity.getPhoneNumber().equals(dto.getPhoneNumber())) {
            return UserVerificationResult.failure(UserVerificationStatus.INVALID_PARAMETER);
        }

        if (DateUtil.isPast(userVerificationEntity.getExpiration())) {
            return UserVerificationResult.failure(UserVerificationStatus.MALFORMED_CODE);

        }

        if (!userVerificationEntity.getCode().equals(dto.getCode())
            || !userVerificationEntity.getCategory().equals(dto.getCategory())) {
            return UserVerificationResult.failure(UserVerificationStatus.INVALID_CODE);
        }

        return UserVerificationResult.success(userVerificationEntity);
    }

    @Transactional
    public void deleteVerificationCode(UserVerificationRequestDto dto) {
        this.userVerificationRepository.deleteByUsername(dto.getUsername());
    }

    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(UserVerificationResult result) {
        UserVerificationStatus successStatus = result.getStatus();

        UserVerificationSuccessResponseDto responseDto = UserVerificationSuccessResponseDto.builder()
                .success(true)
                .status(successStatus.getCode())
                .message(successStatus.getMessage())
                .httpStatusCode(successStatus.getHttpStatusCode().value())
                .username(result.getUsername())
                .email(result.getEmail())
                .exp(result.getExpiration())
                .uid(result.getUid())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getHttpStatusCode()));
    }

    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(UserVerificationResult result) {
        UserVerificationStatus failureStatus = result.getStatus();

        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .status(failureStatus.getCode())
                .message(failureStatus.getMessage())
                .httpStatusCode(failureStatus.getHttpStatusCode().value())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }

    public ResponseEntity<? extends ResponseDto> getNotFoundUserResponseEntity() {
        UserVerificationStatus failureStatus = UserVerificationStatus.UNKNOWN_USER;

        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .status(failureStatus.getCode())
                .message(failureStatus.getMessage())
                .httpStatusCode(failureStatus.getHttpStatusCode().value())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
