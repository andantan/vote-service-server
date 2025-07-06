package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.block.protocol.BlockCreatedEventResponse;
import domain.event.pending.protocol.PendingExpiredEventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc.BlockCreatedEventRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc.PendingExpiredEventRequestDto;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBlockCreatedEventResponseData;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcPendingExpiredEventResponseData;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBlockCreatedEventResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcPendingExpiredEventResponseResult;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBlockCreatedEventResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcPendingExpiredEventResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.stub.BlockEventServiceGrpcStub;
import org.zerock.voteservice.adapter.out.grpc.stub.PendingEventServiceGrpcStub;

@Log4j2
@Component
@RequiredArgsConstructor
public class BlockchainEventProxy {
    private final BlockEventServiceGrpcStub blockEventServiceGrpcStub;
    private final PendingEventServiceGrpcStub pendingEventServiceGrpcStub;

    public GrpcBlockCreatedEventResponseResult reportBlockCreatedEvent(BlockCreatedEventRequestDto dto) {
        GrpcBlockCreatedEventResponseResult result = new GrpcBlockCreatedEventResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcBlockCreatedEventResponseStatus serviceStatus;
        GrpcBlockCreatedEventResponseData data;

        try {
            BlockCreatedEventResponse response = this.blockEventServiceGrpcStub.reportBlockCreatedEvent(
                    dto.getTopic(), dto.getTxCount(), dto.getHeight()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcBlockCreatedEventResponseStatus.fromCode(response.getStatus());
            data = new GrpcBlockCreatedEventResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcBlockCreatedEventResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    blockEventServiceGrpcStub.getLogPrefix(),
                    blockEventServiceGrpcStub.getLayerName(),
                    e.getStatus().getCode(),
                    e.getStatus().getDescription()
            );

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcBlockCreatedEventResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    public GrpcPendingExpiredEventResponseResult reportPendingExpiredEvent(PendingExpiredEventRequestDto dto) {
        GrpcPendingExpiredEventResponseResult result = new GrpcPendingExpiredEventResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcPendingExpiredEventResponseStatus serviceStatus;
        GrpcPendingExpiredEventResponseData data;

        try {
            PendingExpiredEventResponse response = this.pendingEventServiceGrpcStub.reportPendingExpiredEvent(
                    dto.getVoteId(), dto.getVoteCount(), dto.getVoteOptions()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcPendingExpiredEventResponseStatus.fromCode(response.getStatus());
            data = new GrpcPendingExpiredEventResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcPendingExpiredEventResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    pendingEventServiceGrpcStub.getLogPrefix(),
                    pendingEventServiceGrpcStub.getLayerName(),
                    e.getStatus().getCode(),
                    e.getStatus().getDescription()
            );

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcPendingExpiredEventResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }
}
