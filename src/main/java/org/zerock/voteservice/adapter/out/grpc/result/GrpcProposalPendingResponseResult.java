package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalPendingResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalPendingResponseStatus;

public class GrpcProposalPendingResponseResult
    extends AbstractGrpcResponseResult<GrpcProposalPendingResponseStatus, GrpcProposalPendingResponseData> {
}
