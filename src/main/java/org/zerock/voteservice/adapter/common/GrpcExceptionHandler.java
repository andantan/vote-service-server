package org.zerock.voteservice.adapter.common;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.zerock.voteservice.adapter.in.common.ErrorResponseProcessor;
import org.zerock.voteservice.adapter.in.web.dto.common.ResponseDto;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;

@Log4j2
public class GrpcExceptionHandler {

    private GrpcExceptionHandler() {}

    public static <Req> RuntimeException mapStatusRuntimeException(
            StatusRuntimeException e,
            String layer,
            String serviceName,
            String rpcName,
            String grpcHost,
            int grpcPort,
            Req requestMessage
    ) {
        String logPrefix = String.format("[%s:%d] [gRPC-%s-%s::%s] ", grpcHost, grpcPort, layer, serviceName, rpcName);
        Status.Code code = e.getStatus().getCode();

        if (code == Status.Code.UNAVAILABLE) {
            String errorMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    logPrefix, layer, e.getStatus().getCode(), e.getStatus().getDescription());

            return new GrpcServiceUnavailableException(layer, serviceName, rpcName, grpcHost, grpcPort, e.getStatus(), errorMessage, e);
        } else if (code == Status.Code.UNIMPLEMENTED) {
            String errorMessage = String.format("%sgRPC call failed due to executing %s server unimplemented rpc: [Status: %s, Description: \"%s\"]",
                    logPrefix, layer, e.getStatus().getCode(), e.getStatus().getDescription());

            return new GrpcServiceUnavailableException(layer, serviceName, rpcName, grpcHost, grpcPort, e.getStatus(), errorMessage, e);
        } else {
            log.error("{}gRPC call failed with status: {} (Description: {}). Request: {}",
                    logPrefix, e.getStatus().getCode(), e.getStatus().getDescription(), requestMessage, e);

            return e;
        }
    }

    public static ResponseEntity<? extends ResponseDto> handleGrpcStatusRuntimeExceptionInController(
            Integer uid,
            StatusRuntimeException exception,
            ErrorResponseProcessor errorResponseProcessor
    ) {
        String logPrefix = String.format("[UID:%d] ", uid);
        Status.Code statusCode = exception.getStatus().getCode();
        String statusDescription = exception.getStatus().getDescription();

        return switch (statusCode) {
            case DEADLINE_EXCEEDED -> {
                log.error("{}gRPC communication timed out [Status: {}, Description: \"{}\"]",
                        logPrefix, statusCode, statusDescription);
                yield errorResponseProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
            }
            case INVALID_ARGUMENT -> {
                log.warn("{}Invalid argument in gRPC communication [Status: {}, Description: \"{}\"]",
                        logPrefix, statusCode, statusDescription);
                yield errorResponseProcessor.getErrorResponse("INVALID_ARGUMENT");
            }
            case PERMISSION_DENIED -> {
                log.warn("{}Permission denied for gRPC communication [Status: {}, Description: \"{}\"]",
                        logPrefix, statusCode, statusDescription);
                yield errorResponseProcessor.getErrorResponse("PERMISSION_DENIED");
            }
            case UNAUTHENTICATED -> {
                log.warn("{}Unauthenticated gRPC communication [Status: {}, Description: \"{}\"]",
                        logPrefix, statusCode, statusDescription);
                yield errorResponseProcessor.getErrorResponse("UNAUTHENTICATED");
            }
            case INTERNAL -> {
                log.error("{}gRPC server internal error occurred [Status: {}, Description: \"{}\"]",
                        logPrefix, statusCode, statusDescription, exception);
                yield errorResponseProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
            }
            case UNIMPLEMENTED -> {
                log.error("{}Unimplemented rpc executed [Status: {}, Description: \"{}\"]",
                        logPrefix, statusCode, statusDescription);
                yield errorResponseProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
            }
            default -> {
                log.error("{}Unexpected gRPC communication error [Status: {}, Description: \"{}\"]",
                        logPrefix, statusCode, statusDescription, exception);
                yield errorResponseProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
            }
        };
    }
}
