package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotCachingResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotCachingResponseStatus;

public class GrpcBallotCachingResponseResult
    extends AbstractGrpcResponseResult<GrpcBallotCachingResponseStatus, GrpcBallotCachingResponseData> {
}
