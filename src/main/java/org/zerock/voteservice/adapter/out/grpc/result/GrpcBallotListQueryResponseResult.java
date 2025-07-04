package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotListQueryResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotListQueryResponseStatus;

public class GrpcBallotListQueryResponseResult
    extends AbstractGrpcResponseResult<GrpcBallotListQueryResponseStatus, GrpcBallotListQueryResponseData> {
}
