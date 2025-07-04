package org.zerock.voteservice.adapter.in.common;

import org.springframework.http.ResponseEntity;

public interface ErrorResponseProcessor {
    ResponseEntity<? extends ResponseDto> getErrorResponse(String status);
}
