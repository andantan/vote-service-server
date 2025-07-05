package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.UserCachingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.UserCachingSuccessResponseDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.UserCreateProxy;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserCachingResponseResult;

@Log4j2
@Service
public class UserCachingProcessor implements Processor<
        UserCachingRequestDto,
        GrpcUserCachingResponseResult
        > {

    private final UserCreateProxy proxy;

    public UserCachingProcessor(UserCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcUserCachingResponseResult execute(
            UserCachingRequestDto dto
    ) {
        return this.proxy.cacheUser(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            UserCachingRequestDto dto,
            GrpcUserCachingResponseResult result
    ) {
        UserCachingSuccessResponseDto successDto = UserCachingSuccessResponseDto.builder()
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
