package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBlockCreatedEventResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBlockCreatedEventResponseStatus;

public class GrpcBlockCreatedEventResponseResult
    extends AbstractGrpcResponseResult<GrpcBlockCreatedEventResponseStatus, GrpcBlockCreatedEventResponseData> {
}
