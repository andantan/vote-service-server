package org.zerock.voteservice.experiment.out.status;

import org.springframework.http.HttpStatus;

public interface ExperimentGrpcResponseStatus {
    boolean isOk();
    String getCode();
    String getMessage();
    HttpStatus getHttpStatusCode();
}
