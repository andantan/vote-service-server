package org.zerock.voteservice.adapter.in.common;

import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseResult;

public interface Processor<
        In extends RequestDto,
        Out extends GrpcResponseResult
        > {

    Out execute(In dto);

    ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(In dto, Out result);
    ResponseEntity<? extends ResponseDto> getFailureResponseEntity(Out result);
}
