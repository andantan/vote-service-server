package org.zerock.voteservice.adapter.in.web.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.CommonFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.internal.UserInfoRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserInfoSuccessResponseDto;
import org.zerock.voteservice.adapter.out.jpa.entity.UserEntity;
import org.zerock.voteservice.adapter.out.jpa.repository.UserRepository;
import org.zerock.voteservice.tool.hash.Sha256;

import java.util.Optional;

@Service
@Log4j2
public class UserInfoService {
    private final UserRepository userRepository;

    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoServiceResult getUserInfo(UserInfoRequestDto dto) {
        Optional<UserEntity> optionalUser;

        try {
            optionalUser = this.userRepository.findByUid(dto.getUid());
        } catch (Exception e) {
            log.error("Failed to save user to MariaDB: {}", dto.getUsername(), e);
            return UserInfoServiceResult.failure(UserInfoServiceStatus.INTERNAL_SERVER_ERROR);
        }

        if (optionalUser.isEmpty()) {
            return UserInfoServiceResult.failure(UserInfoServiceStatus.NOT_EXIST_UID);
        }

        UserEntity userEntity = optionalUser.get();

        if (!dto.getUsername().equals(userEntity.getUsername())) {
            return UserInfoServiceResult.failure(UserInfoServiceStatus.ABNORMAL_REQUEST);
        }

        if (!dto.getUserhash().equals(Sha256.sum(userEntity))) {
            return UserInfoServiceResult.failure(UserInfoServiceStatus.ABNORMAL_REQUEST);
        }

        return UserInfoServiceResult.success(userEntity);
    }

    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(UserInfoServiceResult result) {
        UserInfoServiceStatus failureStatus = result.getStatus();

        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(false)
                .status(failureStatus.getCode())
                .message(failureStatus.getMessage())
                .httpStatusCode(failureStatus.getHttpStatusCode().value())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }

    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(UserInfoServiceResult result) {
        UserInfoSuccessResponseDto responseDto = UserInfoSuccessResponseDto.builder()
                .uid(result.getUid())
                .userHash(result.getUserHash())
                .userName(result.getUsername())
                .realName(result.getRealName())
                .email(result.getEmail())
                .phoneNumber(result.getPhoneNumber())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getHttpStatusCode()));
    }
}

