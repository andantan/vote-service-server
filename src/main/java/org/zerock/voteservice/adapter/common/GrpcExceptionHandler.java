package org.zerock.voteservice.adapter.common;

import lombok.extern.log4j.Log4j2;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;

@Log4j2
public class GrpcExceptionHandler {

    private GrpcExceptionHandler() {}

    public static GrpcRuntimeStatus mapToGrpcRuntimeStatus(io.grpc.StatusRuntimeException e) {
        return switch (e.getStatus().getCode()) {
            case UNAVAILABLE -> GrpcRuntimeStatus.SERVICE_UNAVAILABLE;
            case DEADLINE_EXCEEDED -> GrpcRuntimeStatus.BAD_GATEWAY;
            default -> GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
