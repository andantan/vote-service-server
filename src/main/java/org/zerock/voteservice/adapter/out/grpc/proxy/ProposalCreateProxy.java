package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.ProposalCachingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.ProposalPendingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.ProposalValidationRequestDto;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalPendingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalValidationResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalCachingResponseData;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalPendingResponseData;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcProposalValidationResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalCachingResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalPendingResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcProposalValidationResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.stub.ProposalPendingServiceGrpcStub;
import org.zerock.voteservice.adapter.out.grpc.stub.ProposalCreateEventServiceGrpcStub;

@Log4j2
@Component
public class ProposalCreateProxy {
    private final ProposalCreateEventServiceGrpcStub cahceServerStub;
    private final ProposalPendingServiceGrpcStub blockchainNodeStub;

    public ProposalCreateProxy(
            ProposalCreateEventServiceGrpcStub cahceServerStub,
            ProposalPendingServiceGrpcStub blockchainNodeStub
    ) {
        this.cahceServerStub = cahceServerStub;
        this.blockchainNodeStub = blockchainNodeStub;
    }

    public GrpcProposalValidationResponseResult validateProposal(ProposalValidationRequestDto dto) {
        GrpcProposalValidationResponseResult result = new GrpcProposalValidationResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcProposalValidationResponseStatus serviceStatus;
        GrpcProposalValidationResponseData data;

        try {
            ProposalValidateEventResponse response = this.cahceServerStub.validateProposal(dto.getTopic());

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcProposalValidationResponseStatus.fromCode(response.getStatus());
            data = new GrpcProposalValidationResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcProposalValidationResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cahceServerStub.getLogPrefix(), cahceServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcProposalValidationResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    public GrpcProposalPendingResponseResult pendingProposal(ProposalPendingRequestDto dto) {
        GrpcProposalPendingResponseResult result =  new GrpcProposalPendingResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcProposalPendingResponseStatus serviceStatus;
        GrpcProposalPendingResponseData data;

        try {
            OpenProposalPendingResponse response = this.blockchainNodeStub.openProposalPending(
                    dto.getTopic(), dto.getDuration()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcProposalPendingResponseStatus.fromCode(response.getStatus());
            data = new GrpcProposalPendingResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcProposalPendingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    blockchainNodeStub.getLogPrefix(), blockchainNodeStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcProposalPendingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    public GrpcProposalCachingResponseResult cachingProposal(ProposalCachingRequestDto dto) {
        GrpcProposalCachingResponseResult result = new GrpcProposalCachingResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcProposalCachingResponseStatus serviceStatus;
        GrpcProposalCachingResponseData data;

        try {
            ProposalCacheEventResponse response = this.cahceServerStub.cacheProposal(
                    dto.getTopic(), dto.getDuration(), dto.getOptions()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcProposalCachingResponseStatus.fromCode(response.getStatus());
            data = new GrpcProposalCachingResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcProposalCachingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cahceServerStub.getLogPrefix(), cahceServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcProposalCachingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }
}
