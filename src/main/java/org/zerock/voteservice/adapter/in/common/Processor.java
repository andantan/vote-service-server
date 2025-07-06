package org.zerock.voteservice.adapter.in.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.adapter.in.common.extend.CommonFailureResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.GrpcRequestDto;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseResult;

public interface Processor<
        In extends GrpcRequestDto,
        Out extends GrpcResponseResult
        > {

    Out execute(In dto);

    ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(In dto, Out result);

    default ResponseEntity<? extends ResponseDto> getFailureResponseEntity(Out result) {
        CommonFailureResponseDto failureDto = CommonFailureResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();


        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
