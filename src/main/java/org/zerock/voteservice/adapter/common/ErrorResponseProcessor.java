package org.zerock.voteservice.adapter.common;

import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;

public interface ErrorResponseProcessor {
    ResponseEntity<? extends ResponseDto> getErrorResponse(String status);
}
