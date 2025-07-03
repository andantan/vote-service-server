package org.zerock.voteservice.experiment.in.domain.data;

import org.springframework.http.HttpStatus;

public interface ExperimentResult {
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
