package org.zerock.voteservice.adapter.out.grpc.common.exception;

import io.grpc.Status;
import lombok.Getter;

@Getter
public class GrpcServiceUnavailableException extends RuntimeException {
    private final String layer;
    private final String serviceName;
    private final String rpcName;
    private final String host;
    private final Status status;
    private final int port;

    public GrpcServiceUnavailableException(
            String layer,
            String serviceName,
            String rpcName,
            String host,
            int port,
            Status status,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.layer = layer;
        this.serviceName = serviceName;
        this.rpcName = rpcName;
        this.host = host;
        this.port = port;
        this.status = status;
    }

    @Override
    public String toString() {
        return "GrpcServiceUnavailableException{" +
                "layer='" + layer + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", methodName='" + rpcName + '\'' +
                ", status.Code=" + status.getCode() +
                ", message='" + getLocalizedMessage() + '\'' +
                '}';
    }
}

