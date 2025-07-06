package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalValidationResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalValidationResponseStatus;

public class GrpcProposalValidationResponseResult
    extends AbstractGrpcResponseResult<GrpcProposalValidationResponseStatus, GrpcProposalValidationResponseData> {
}
