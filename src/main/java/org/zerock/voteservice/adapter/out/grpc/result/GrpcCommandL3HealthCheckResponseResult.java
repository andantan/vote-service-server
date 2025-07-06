package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcCommandL3HealthCheckResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcCommandL3ResponseStatus;

public class GrpcCommandL3HealthCheckResponseResult
    extends AbstractGrpcResponseResult<GrpcCommandL3ResponseStatus, GrpcCommandL3HealthCheckResponseData> {
}
