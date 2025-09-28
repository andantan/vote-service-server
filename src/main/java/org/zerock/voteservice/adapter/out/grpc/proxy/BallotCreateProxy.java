package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.ballot.create.protocol.BallotCacheEventResponse;
import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotCachingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotTransactionGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotValidationGrpcRequestDto;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotTransactionResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotValidationResponseResult;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotCachingResponseData;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotTransactionResponseData;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotValidationResponseData;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotCachingResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotTransactionResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotValidationResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.stub.GrpcBallotTransactionServiceStub;
import org.zerock.voteservice.adapter.out.grpc.stub.GrpcBallotCreateEventServiceStub;

@Log4j2
@Component
@RequiredArgsConstructor
public class BallotCreateProxy {
    private final GrpcBallotTransactionServiceStub blockchainNodeStub;
    private final GrpcBallotCreateEventServiceStub cacheServerStub;

    public GrpcBallotValidationResponseResult validateBallot(
            BallotValidationGrpcRequestDto dto
    ) {
        GrpcBallotValidationResponseResult result = new GrpcBallotValidationResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcBallotValidationResponseStatus serviceStatus;
        GrpcBallotValidationResponseData data;

        try {
            BallotValidateEventResponse response = this.cacheServerStub.validateBallot(
                    dto.getUserHash(), dto.getTopic(), dto.getOption()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcBallotValidationResponseStatus.fromCode(response.getStatus());
            data = new GrpcBallotValidationResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcBallotValidationResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cacheServerStub.getLogPrefix(), cacheServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcBallotValidationResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    public GrpcBallotTransactionResponseResult submitBallotTransaction(
            BallotTransactionGrpcRequestDto dto
    ) {
        GrpcBallotTransactionResponseResult result = new GrpcBallotTransactionResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcBallotTransactionResponseStatus serviceStatus;
        GrpcBallotTransactionResponseData data;

        try {
            SubmitBallotTransactionResponse response = this.blockchainNodeStub.submitBallotTransaction(
                    dto.getUserHash(), dto.getTopic(), dto.getOption(), dto.getSalt()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcBallotTransactionResponseStatus.fromCode(response.getStatus());
            data = new GrpcBallotTransactionResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcBallotTransactionResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    blockchainNodeStub.getLogPrefix(), blockchainNodeStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcBallotTransactionResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    public GrpcBallotCachingResponseResult cacheBallot(
            BallotCachingGrpcRequestDto dto
    ) {
        GrpcBallotCachingResponseResult result = new GrpcBallotCachingResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcBallotCachingResponseStatus serviceStatus;
        GrpcBallotCachingResponseData data;

        try {
            BallotCacheEventResponse response = this.cacheServerStub.cacheBallot(
                    dto.getUserHash(), dto.getVoteHash(), dto.getTopic()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcBallotCachingResponseStatus.fromCode(response.getStatus());
            data = new GrpcBallotCachingResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcBallotCachingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cacheServerStub.getLogPrefix(), cacheServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcBallotCachingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }
}
