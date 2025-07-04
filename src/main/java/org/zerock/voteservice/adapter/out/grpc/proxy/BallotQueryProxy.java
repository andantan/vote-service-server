package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcBallotListQueryResponseData;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotListQueryResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotListQueryRequestDto;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcBallotListQueryResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.stub.BallotQueryEventServiceGrpcStub;

@Log4j2
@Component
public class BallotQueryProxy {

    private final BallotQueryEventServiceGrpcStub cahceServerStub;

    public BallotQueryProxy(
            BallotQueryEventServiceGrpcStub cahceServerStub
    ) {
        this.cahceServerStub = cahceServerStub;
    }

    public GrpcBallotListQueryResponseResult getBallotList(BallotListQueryRequestDto dto) {
        GrpcBallotListQueryResponseResult result = new GrpcBallotListQueryResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcBallotListQueryResponseStatus serviceStatus;
        GrpcBallotListQueryResponseData data;

        try {
            GetUserBallotsResponse response = this.cahceServerStub.getUserBallots(dto.getUserHash());

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcBallotListQueryResponseStatus.fromCode(response.getStatus());
            data = new GrpcBallotListQueryResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcBallotListQueryResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cahceServerStub.getLogPrefix(), cahceServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcBallotListQueryResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }
}
