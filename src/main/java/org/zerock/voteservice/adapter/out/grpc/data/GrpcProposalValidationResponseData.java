package org.zerock.voteservice.adapter.out.grpc.data;

import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseData;

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
