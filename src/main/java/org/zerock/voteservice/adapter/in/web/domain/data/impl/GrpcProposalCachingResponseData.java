package org.zerock.voteservice.adapter.in.web.domain.data.impl;

import domain.event.proposal.create.protocol.ProposalCacheEventResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcProposalCachingResponseData implements GrpcResponseData {
    Boolean success;
    String status;

    public GrpcProposalCachingResponseData(ProposalCacheEventResponse response) {
        this.success = response.getCached();
        this.status = response.getStatus();
    }
}
