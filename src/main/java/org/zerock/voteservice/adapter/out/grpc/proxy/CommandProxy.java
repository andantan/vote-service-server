package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.admin.l3.protocol.L3HealthCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL3HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcCommandL3HealthCheckResponseData;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL3HealthCheckResponseResult;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcCommandL3ResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.stub.GrpcL3CommandServiceStub;

@Log4j2
@Component
@RequiredArgsConstructor
public class CommandProxy {

    private final GrpcL3CommandServiceStub cacheServerStub;

    public GrpcCommandL3HealthCheckResponseResult L3CheckHealth(
        CommandL3HealthCheckGrpcRequestDto dto
    ) {
        GrpcCommandL3HealthCheckResponseResult result = new GrpcCommandL3HealthCheckResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcCommandL3ResponseStatus serviceStatus;
        GrpcCommandL3HealthCheckResponseData data;

        try {
            L3HealthCheckResponse response = this.cacheServerStub.checkHealth(dto.getPing());

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcCommandL3ResponseStatus.fromCode(response.getStatus());
            data = new GrpcCommandL3HealthCheckResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcCommandL3ResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cacheServerStub.getLogPrefix(), cacheServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcCommandL3ResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }
}
