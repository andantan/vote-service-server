package org.zerock.voteservice.adapter.in.web.processor;

import com.google.protobuf.Timestamp;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.user.create.protocol.UserValidateEventResponse;
import domain.event.user.create.protocol.UserCacheEventResponse;

import org.zerock.voteservice.adapter.in.common.ErrorResponseProcessor;
import org.zerock.voteservice.adapter.in.web.dto.user.error.UserErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.user.error.status.UserRegisterErrorStatus;
import org.zerock.voteservice.adapter.in.web.dto.UserCacheRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.UserRegisterRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.UserRegisterResponseDto;
import org.zerock.voteservice.adapter.in.web.service.UserRegisterServiceResult;

import org.zerock.voteservice.adapter.out.grpc.proxy.UserCreateProxy;

import org.zerock.voteservice.tool.time.DateConverter;

@Log4j2
@Service
public class UserRegisterProcessor implements ErrorResponseProcessor {

    private final UserCreateProxy userCreateProxy;

    public UserRegisterProcessor(UserCreateProxy userCreateProxy) {
        this.userCreateProxy = userCreateProxy;
    }

    public UserRegisterProcessorResult processUserCreation(UserRegisterServiceResult serviceResult) {
        UserCacheRequestDto dto = this.extractCacheDto(serviceResult);

        UserValidateEventResponse validatedUser = this.userCreateProxy.validateUser(dto);

        if (!validatedUser.getValidation()) {
            return UserRegisterProcessorResult.failure(validatedUser.getStatus());
        }

        UserCacheEventResponse cachedUser = this.userCreateProxy.cacheUser(dto);

        if (!cachedUser.getCached()) {
            return UserRegisterProcessorResult.failure(cachedUser.getStatus());
        }

        return UserRegisterProcessorResult.success(
                cachedUser.getStatus(), serviceResult.getUid(), serviceResult.getUserHash()
        );
    }

    private UserCacheRequestDto extractCacheDto(UserRegisterServiceResult serviceResult) {
        Timestamp birthDateTimeStamp = DateConverter.toTimestamp(serviceResult.getBirthDate());

        return UserCacheRequestDto.builder()
                .uid(serviceResult.getUid())
                .userHash(serviceResult.getUserHash())
                .gender(serviceResult.getGender())
                .birthDate(birthDateTimeStamp)
                .build();
    }

    public ResponseEntity<UserRegisterResponseDto> getSuccessDto(
            UserRegisterRequestDto dto, UserRegisterProcessorResult result
    ) {
        UserRegisterResponseDto successDto = UserRegisterResponseDto.builder()
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .uid(result.getUid())
                .userHash(result.getUserHash())
                .username(dto.getUsername())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<UserErrorResponseDto> getErrorResponse(
            UserRegisterProcessorResult result
    ) {
        UserRegisterErrorStatus errorStatus = UserRegisterErrorStatus.fromCode(result.getStatus());
        UserErrorResponseDto errorDto = UserErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, errorStatus.getHttpStatusCode());
    }

    public ResponseEntity<UserErrorResponseDto> getErrorResponse(
            String status
    ) {
        UserRegisterErrorStatus errorStatus = UserRegisterErrorStatus.fromCode(status);
        UserErrorResponseDto errorDto = UserErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, errorStatus.getHttpStatusCode());
    }
}
