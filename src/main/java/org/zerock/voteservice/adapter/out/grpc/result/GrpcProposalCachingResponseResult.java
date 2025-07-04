package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalCachingResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalCachingResponseStatus;

public class GrpcProposalCachingResponseResult
    extends AbstractGrpcResponseResult<GrpcProposalCachingResponseStatus, GrpcProposalCachingResponseData> {
}
