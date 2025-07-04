package org.zerock.voteservice.adapter.in.web.domain.data;

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
