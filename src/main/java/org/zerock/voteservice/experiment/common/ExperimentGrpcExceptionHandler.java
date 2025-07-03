package org.zerock.voteservice.experiment.common;

import io.grpc.StatusRuntimeException;
import org.zerock.voteservice.experiment.out.status.ExperimentExternalGrpcRuntimeStatus;

public class ExperimentGrpcExceptionHandler {
    public static ExperimentExternalGrpcRuntimeStatus mapStatusRuntimeExceptionToExternalStatus(StatusRuntimeException e) {
        return switch (e.getStatus().getCode()) {
            case UNAVAILABLE -> ExperimentExternalGrpcRuntimeStatus.SERVICE_UNAVAILABLE;
            case DEADLINE_EXCEEDED -> ExperimentExternalGrpcRuntimeStatus.BAD_GATEWAY;
            default -> ExperimentExternalGrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
