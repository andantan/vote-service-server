package org.zerock.voteservice.adapter.out.grpc.proxy.query;

import domain.event.proposal.query.protocol.Paging;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import domain.event.proposal.query.protocol.Filter;
import domain.event.proposal.query.protocol.GetProposalDetailResponse;
import domain.event.proposal.query.protocol.GetFilteredProposalListResponse;

import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalDetailRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalFilteredListRequestDto;
import org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData.ProposalQueryEventServiceGrpcStub;

@Log4j2
@Service
public class ProposalQueryProxy {

    private final ProposalQueryEventServiceGrpcStub proposalQueryEventServiceGrpcStub;

    public ProposalQueryProxy(
            ProposalQueryEventServiceGrpcStub proposalQueryEventServiceGrpcStub
    ) {
        this.proposalQueryEventServiceGrpcStub = proposalQueryEventServiceGrpcStub;
    }

    public GetProposalDetailResponse getProposalDetail(QueryProposalDetailRequestDto dto) {
        return this.proposalQueryEventServiceGrpcStub.getProposalDetail(dto.getTopic());
    }

    public GetFilteredProposalListResponse getFilteredProposalList(QueryProposalFilteredListRequestDto dto) {
        Filter filter = this.extractFilter(dto);
        Paging paging = Paging.newBuilder()
                .setSkip(dto.getSkip())
                .setLimit(dto.getLimit())
                .build();

        return this.proposalQueryEventServiceGrpcStub.getFilteredProposalList(filter, paging);
    }

    private Filter extractFilter(QueryProposalFilteredListRequestDto dto) {
        Filter.Builder filterBuilder = Filter.newBuilder();

        if (dto.isExpiredFiltered()) {
            filterBuilder.setExpired(dto.getExpired());
        }

        return filterBuilder.build();
    }
}
