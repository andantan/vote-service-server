package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcPendingExpiredEventResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcPendingExpiredEventResponseStatus;

public class GrpcPendingExpiredEventResponseResult
    extends AbstractGrpcResponseResult<GrpcPendingExpiredEventResponseStatus, GrpcPendingExpiredEventResponseData> {
}
