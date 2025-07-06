package org.zerock.voteservice.adapter.out.grpc.common;

import org.springframework.http.HttpStatus;

public interface GrpcResponseStatus {
    boolean isOk();
    String getCode();
    String getMessage();
    HttpStatus getHttpStatusCode();
}
