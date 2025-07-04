package org.zerock.voteservice.adapter.in.web.processor;

import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.RequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;

public interface Processor<
        In extends RequestDto,
        Out extends GrpcResponseResult
        > {

    Out process(In dto);

    ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(In dto, Out result);
    ResponseEntity<? extends ResponseDto> getFailureResponseEntity(Out result);
}
