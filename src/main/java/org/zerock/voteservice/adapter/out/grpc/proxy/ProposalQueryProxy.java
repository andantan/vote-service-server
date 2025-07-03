package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.proposal.query.protocol.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.adapter.in.web.dto.QueryProposalDetailRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.QueryProposalFilteredListRequestDto;
import org.zerock.voteservice.adapter.out.grpc.stub.ProposalQueryEventServiceGrpcStub;

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
        Sort sort = this.extractSort(dto);
        Paging paging = Paging.newBuilder()
                .setSkip(dto.getSkip())
                .setLimit(dto.getLimit())
                .build();

        return this.proposalQueryEventServiceGrpcStub.getFilteredProposalList(filter, sort, paging);
    }

    private Filter extractFilter(QueryProposalFilteredListRequestDto dto) {
        Filter.Builder filterBuilder = Filter.newBuilder();

        if (dto.isExpiredFiltered()) {
            filterBuilder.setExpired(dto.getExpired());
        }

        return filterBuilder.build();
    }

    public Sort extractSort(QueryProposalFilteredListRequestDto dto) {
        Sort.Builder sortBuilder = Sort.newBuilder();

        if (dto.isSortFiltered()) {
            sortBuilder.setSortOrder(dto.getSortOrder());
            sortBuilder.setSortBy((dto.getSortBy()));
        }

        return sortBuilder.build();
    }
}
