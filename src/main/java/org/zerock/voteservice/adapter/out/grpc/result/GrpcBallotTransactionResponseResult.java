package org.zerock.voteservice.adapter.out.grpc.result;

import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotTransactionResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotTransactionResponseStatus;

public class GrpcBallotTransactionResponseResult
    extends AbstractGrpcResponseResult<GrpcBallotTransactionResponseStatus, GrpcBallotTransactionResponseData> {

    public String getVoteHash() {
        return this.getGrpcResponseData().getVoteHash();
    }
}
