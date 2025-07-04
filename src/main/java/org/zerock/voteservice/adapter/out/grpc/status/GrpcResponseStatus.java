package org.zerock.voteservice.adapter.out.grpc.status;

import org.springframework.http.HttpStatus;

public interface GrpcResponseStatus {
    boolean isOk();
    String getCode();
    String getMessage();
    HttpStatus getHttpStatusCode();
}
