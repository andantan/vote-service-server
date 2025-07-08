package org.zerock.voteservice.adapter.out.grpc.proxy;

import domain.event.admin.L3.protocol.L3HealthCheckResponse;
import domain.event.admin.L4.protocol.L4HealthCheckResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL3HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.in.admin.domain.request.grpc.CommandL4HealthCheckGrpcRequestDto;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcCommandL3HealthCheckResponseData;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcCommandL4HealthCheckResponseData;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL3HealthCheckResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcCommandL4HealthCheckResponseResult;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcCommandL3ResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcCommandL4ResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.stub.GrpcL3CommandServiceStub;
import org.zerock.voteservice.adapter.out.grpc.stub.GrpcL4CommandServiceStub;

@Log4j2
@Component
@RequiredArgsConstructor
public class CommandProxy {

    private final GrpcL3CommandServiceStub cacheServerStub;
    private final GrpcL4CommandServiceStub blockchainNodeStub;

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

    public GrpcCommandL4HealthCheckResponseResult L4CheckHealth(
            CommandL4HealthCheckGrpcRequestDto dto
    ) {
        GrpcCommandL4HealthCheckResponseResult result = new GrpcCommandL4HealthCheckResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcCommandL4ResponseStatus serviceStatus;
        GrpcCommandL4HealthCheckResponseData data;

        try {
            L4HealthCheckResponse response = blockchainNodeStub.checkHealth(dto.getPing());

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcCommandL4ResponseStatus.fromCode(response.getStatus());
            data = new GrpcCommandL4HealthCheckResponseData(response);

        }  catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcCommandL4ResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cacheServerStub.getLogPrefix(), cacheServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcCommandL4ResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }
}
