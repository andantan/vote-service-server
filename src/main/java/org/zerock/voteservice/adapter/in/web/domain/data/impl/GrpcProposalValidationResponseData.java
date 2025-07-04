package org.zerock.voteservice.adapter.in.web.domain.data.impl;

import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcProposalValidationResponseData implements GrpcResponseData {
    Boolean success;
    String status;

    public GrpcProposalValidationResponseData(ProposalValidateEventResponse response) {
        this.success = response.getValidation();
        this.status = response.getStatus();
    }
}
