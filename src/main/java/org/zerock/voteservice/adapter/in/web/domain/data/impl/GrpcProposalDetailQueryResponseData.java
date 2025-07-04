package org.zerock.voteservice.adapter.in.web.domain.data.impl;

import lombok.*;

import domain.event.proposal.query.protocol.Proposal;
import domain.event.proposal.query.protocol.GetProposalDetailResponse;
import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcProposalDetailQueryResponseData implements GrpcResponseData {
    Boolean success;
    String status;
    Proposal proposal;

    public GrpcProposalDetailQueryResponseData(GetProposalDetailResponse response) {
        this.success = response.getQueried();
        this.status = response.getStatus();
        this.proposal = response.getProposal();
    }
}
