package org.zerock.voteservice.adapter.in.common;

import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.adapter.in.common.extend.RestApiRequestDto;

public interface Orchestrator<In extends RestApiRequestDto, Out extends ResponseDto> {
    ResponseEntity<? extends Out> orchestrate(In requestDto);
}
