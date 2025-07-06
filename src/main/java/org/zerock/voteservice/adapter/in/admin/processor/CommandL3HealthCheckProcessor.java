package org.zerock.voteservice.adapter.in.admin.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL3HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.response.CommandL3HealthCheckSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.CommandProxy;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL3HealthCheckResponseResult;

@Log4j2
@Service
@RequiredArgsConstructor
public class CommandL3HealthCheckProcessor implements Processor<
        CommandL3HealthCheckGrpcRequestDto,
        GrpcCommandL3HealthCheckResponseResult
        > {

    private final CommandProxy proxy;

    @Override
    public GrpcCommandL3HealthCheckResponseResult execute(
            CommandL3HealthCheckGrpcRequestDto dto
    ) {
        return proxy.L3CheckHealth(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            CommandL3HealthCheckGrpcRequestDto dto,
            GrpcCommandL3HealthCheckResponseResult result
    ) {
        CommandL3HealthCheckSuccessResponseDto successDto = CommandL3HealthCheckSuccessResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .ping(dto.getPing())
                .pong(result.getData().getPong())
                .ip(result.getData().getIp())
                .ports(result.getData().getPorts())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }
}
