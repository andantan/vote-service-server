package org.zerock.voteservice.adapter.in.common;

import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;

public interface ErrorResponseProcessor {
    ResponseEntity<? extends ResponseDto> getErrorResponse(String status);
}
