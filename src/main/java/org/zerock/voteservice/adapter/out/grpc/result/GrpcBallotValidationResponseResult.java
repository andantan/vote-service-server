package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotValidationResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotValidationResponseStatus;

public class GrpcBallotValidationResponseResult
    extends AbstractGrpcResponseResult<GrpcBallotValidationResponseStatus, GrpcBallotValidationResponseData> {
}
