package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalDetailQueryResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalDetailQueryResponseStatus;

public class GrpcProposalDetailQueryResponseResult
    extends AbstractGrpcResponseResult<GrpcProposalDetailQueryResponseStatus, GrpcProposalDetailQueryResponseData> {
}
