package org.zerock.voteservice.adapter.in.web.domain.data.impl;

import lombok.*;
import java.util.List;

import domain.event.proposal.query.protocol.Proposal;
import domain.event.proposal.query.protocol.GetFilteredProposalListResponse;

import org.zerock.voteservice.adapter.in.web.domain.data.GrpcResponseData;


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
