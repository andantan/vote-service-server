package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcCommandL4HealthCheckResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcCommandL4ResponseStatus;

public class GrpcCommandL4HealthCheckResponseResult
    extends AbstractGrpcResponseResult<GrpcCommandL4ResponseStatus, GrpcCommandL4HealthCheckResponseData> {
}
