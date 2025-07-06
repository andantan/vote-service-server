package org.zerock.voteservice.adapter.out.grpc.common;

import org.springframework.http.HttpStatus;

public interface GrpcResponseResult {
    Boolean getSuccess();
    String getMessage();
    String getStatus();
    Integer getHttpStatusCode();

    String getGrpcServerStatusCode();
    String getGrpcServerStatusMessage();
    HttpStatus getGrpcServerStatusStatusCode();

    String getGrpcResponseStatusCode();
    String getGrpcResponsetatusMessage();
    HttpStatus getGrpcResponseStatusStatusCode();

    Boolean isGrpcServerError();
    Boolean isGrpcResponseError();
}
