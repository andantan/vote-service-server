package org.zerock.voteservice.experiment.in.domain.data;

import domain.event.proposal.query.protocol.GetProposalDetailResponse;
import lombok.*;
import domain.event.proposal.query.protocol.Proposal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentProposalDetailQueryData implements ExperimentData {
    Boolean success;
    String status;
    Proposal proposal;

    public ExperimentProposalDetailQueryData(GetProposalDetailResponse response) {
        this.success = response.getQueried();
        this.status = response.getStatus();
        this.proposal = response.getProposal();
    }
}
