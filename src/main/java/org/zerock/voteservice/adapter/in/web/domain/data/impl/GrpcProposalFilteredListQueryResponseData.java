package org.zerock.voteservice.adapter.in.web.domain.data.impl;

import domain.event.proposal.query.protocol.GetFilteredProposalListResponse;
import domain.event.proposal.query.protocol.Proposal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseData;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrpcProposalFilteredListQueryResponseData implements GrpcResponseData {
    Boolean success;
    String status;
    List<Proposal> proposalList;

    public GrpcProposalFilteredListQueryResponseData(GetFilteredProposalListResponse response) {
        this.success = response.getQueried();
        this.status = response.getStatus();
        this.proposalList = response.getProposalListList();
    }

}
