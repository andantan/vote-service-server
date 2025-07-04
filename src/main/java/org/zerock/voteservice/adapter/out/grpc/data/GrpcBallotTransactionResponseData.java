package org.zerock.voteservice.adapter.out.grpc.data;

import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcBallotTransactionResponseData implements GrpcResponseData {
    Boolean success;
    String status;
    String voteHash;

    public GrpcBallotTransactionResponseData(SubmitBallotTransactionResponse response) {
        this.success = response.getSuccess();
        this.status = response.getStatus();
        this.voteHash = response.getVoteHash();
    }
}
