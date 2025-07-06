package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalFilteredListQueryResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalFilteredListQueryResponseStatus;

public class GrpcProposalFilteredListQueryResponseResult
    extends AbstractGrpcResponseResult<GrpcProposalFilteredListQueryResponseStatus, GrpcProposalFilteredListQueryResponseData> {
}
