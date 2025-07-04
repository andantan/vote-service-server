package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.proposal.query.protocol.*;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;

import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalDetailQueryResponseData;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalDetailQueryResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalFilteredListQueryResponseData;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalFilteredListQueryResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQueryRequestDto;

import org.zerock.voteservice.adapter.out.grpc.stub.ProposalQueryEventServiceGrpcStub;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalDetailQueryResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalFilteredListQueryResponseStatus;

@Log4j2
@Component
public class ProposalQueryProxy {

    private final ProposalQueryEventServiceGrpcStub cahceServerStub;

    public ProposalQueryProxy(
            ProposalQueryEventServiceGrpcStub cacheServerStub
    ) {
        this.cahceServerStub = cacheServerStub;
    }

    public GrpcProposalDetailQueryResponseResult getProposalDetail(ProposalDetailQueryRequestDto dto) {
        GrpcProposalDetailQueryResponseResult result = new GrpcProposalDetailQueryResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcProposalDetailQueryResponseStatus serviceStatus;
        GrpcProposalDetailQueryResponseData data;

        try {
            GetProposalDetailResponse response = this.cahceServerStub.getProposalDetail(dto.getTopic());

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcProposalDetailQueryResponseStatus.fromCode(response.getStatus());
            data = new GrpcProposalDetailQueryResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcProposalDetailQueryResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cahceServerStub.getLogPrefix(), cahceServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcProposalDetailQueryResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    public GrpcProposalFilteredListQueryResponseResult getFilteredProposalList(ProposalFilteredListQueryRequestDto dto) {
        GrpcProposalFilteredListQueryResponseResult result = new GrpcProposalFilteredListQueryResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcProposalFilteredListQueryResponseStatus serviceStatus;
        GrpcProposalFilteredListQueryResponseData data;

        Filter filter = this.extractFilter(dto);
        Sort sort = this.extractSort(dto);
        Paging paging = Paging.newBuilder()
                .setSkip(dto.getSkip())
                .setLimit(dto.getLimit())
                .build();

        try {
            GetFilteredProposalListResponse response = this.cahceServerStub.getFilteredProposalList(filter, sort, paging);

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcProposalFilteredListQueryResponseStatus.fromCode(response.getStatus());
            data = new GrpcProposalFilteredListQueryResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcProposalFilteredListQueryResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cahceServerStub.getLogPrefix(), cahceServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcProposalFilteredListQueryResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    private Filter extractFilter(ProposalFilteredListQueryRequestDto dto) {
        Filter.Builder filterBuilder = Filter.newBuilder();

        if (dto.isExpiredFiltered()) {
            filterBuilder.setExpired(dto.getExpired());
        }

        return filterBuilder.build();
    }

    public Sort extractSort(ProposalFilteredListQueryRequestDto dto) {
        Sort.Builder sortBuilder = Sort.newBuilder();

        if (dto.isSortFiltered()) {
            sortBuilder.setSortOrder(dto.getSortOrder());
            sortBuilder.setSortBy((dto.getSortBy()));
        }

        return sortBuilder.build();
    }
}
