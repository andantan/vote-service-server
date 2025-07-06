package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.UserValidationGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserValidationSuccessResponseDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.UserCreateProxy;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserValidationResponseResult;

@Log4j2
@Service
public class UserValidationProcessor implements Processor<
        UserValidationGrpcRequestDto,
        GrpcUserValidationResponseResult
        > {

    private final UserCreateProxy proxy;

    public UserValidationProcessor(UserCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcUserValidationResponseResult execute(
            UserValidationGrpcRequestDto dto
    ) {
        return this.proxy.validateUser(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            UserValidationGrpcRequestDto dto,
            GrpcUserValidationResponseResult result
    ) {
        UserValidationSuccessResponseDto successDto = UserValidationSuccessResponseDto.builder()
                .uid(dto.getUid())
                .userHash(dto.getUserHash())
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));

    }
}
