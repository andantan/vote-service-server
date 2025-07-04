package org.zerock.voteservice.adapter.in.web.domain.data.impl;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcProposalPendingResponseData implements GrpcResponseData {
    Boolean success;
    String status;
    String message;

    public GrpcProposalPendingResponseData(OpenProposalPendingResponse response) {
        this.success = response.getSuccess();
        this.status = response.getStatus();
        this.message = response.getMessage();
    }
}
