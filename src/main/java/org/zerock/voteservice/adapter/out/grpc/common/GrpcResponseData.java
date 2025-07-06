package org.zerock.voteservice.adapter.out.grpc.common;

public interface GrpcResponseData {
    Boolean getSuccess();
    String getStatus();
}