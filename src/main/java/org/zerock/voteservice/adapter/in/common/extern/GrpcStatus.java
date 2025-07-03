package org.zerock.voteservice.adapter.in.common.extern;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GrpcStatus {
    private final Layer layer;
    private final GrpcServerStatus grpcServerStatus;

    public GrpcStatus(Layer layer, GrpcServerStatus grpcServerStatus) {
        this.layer = layer;
        this.grpcServerStatus = grpcServerStatus;
    }

    public static GrpcStatus of(Layer layer, GrpcServerStatus grpcServerStatus) {
        return new GrpcStatus(layer, grpcServerStatus);
    }

    public static GrpcStatus ofMongoDB(GrpcServerStatus grpcServerStatus) {
        return of(Layer.MONGODB_CACHE, grpcServerStatus);
    }

    public static GrpcStatus ofBlockchainNode(GrpcServerStatus grpcServerStatus) {
        return of(Layer.BLOCKCHAIN_NODE, grpcServerStatus);
    }

    public String getCode() {
        return this.grpcServerStatus.getCode();
    }

    public HttpStatus getHttpStatus() {
        return this.grpcServerStatus.getHttpStatusCode();
    }

    public String getMessageByLayerCode() {
        return this.grpcServerStatus.getFormattedMessageByLayerCode(this.layer);
    }

    public String getMessageByLayerName() {
        return this.grpcServerStatus.getFormattedMessageByLayerName(this.layer);
    }

    public String getMessageByFullName() {
        return this.grpcServerStatus.getFormattedMessageByFullName(this.layer);
    }

    public boolean isSuccess() {
        return this.grpcServerStatus == GrpcServerStatus.OK;
    }
}
