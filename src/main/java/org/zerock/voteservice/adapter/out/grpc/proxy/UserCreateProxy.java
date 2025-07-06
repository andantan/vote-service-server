package org.zerock.voteservice.adapter.out.grpc.proxy;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import domain.event.user.create.protocol.UserValidateEventResponse;
import domain.event.user.create.protocol.UserCacheEventResponse;

import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.UserCachingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.UserValidationRequestDto;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcUserCachingResponseData;
import org.zerock.voteservice.adapter.out.grpc.data.GrpcUserValidationResponseData;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcUserValidationResponseResult;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcRuntimeStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcUserCachingResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.status.GrpcUserValidationResponseStatus;
import org.zerock.voteservice.adapter.out.grpc.stub.UserCreateEventServiceGrpcStub;

@Log4j2
@Component
public class UserCreateProxy {
    private final UserCreateEventServiceGrpcStub cahceServerStub;

    public UserCreateProxy(
            UserCreateEventServiceGrpcStub cahceServerStub
    ) {
        this.cahceServerStub = cahceServerStub;
    }

    public GrpcUserValidationResponseResult validateUser(
            UserValidationRequestDto dto
    ) {
        GrpcUserValidationResponseResult result = new GrpcUserValidationResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcUserValidationResponseStatus serviceStatus;
        GrpcUserValidationResponseData data;

        try {
            UserValidateEventResponse response = this.cahceServerStub.validateUser(
                    dto.getUid(), dto.getUserHash()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcUserValidationResponseStatus.fromCode(response.getStatus());
            data = new GrpcUserValidationResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcUserValidationResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cahceServerStub.getLogPrefix(), cahceServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcUserValidationResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }

    public GrpcUserCachingResponseResult cacheUser(UserCachingRequestDto dto) {
        GrpcUserCachingResponseResult result = new GrpcUserCachingResponseResult();

        GrpcRuntimeStatus serverStatus;
        GrpcUserCachingResponseStatus serviceStatus;
        GrpcUserCachingResponseData data;

        try {
            UserCacheEventResponse response = this.cahceServerStub.cacheUser(
                    dto.getUid(), dto.getUserHash(), dto.getGender(), dto.getBirthDate()
            );

            serverStatus = GrpcRuntimeStatus.OK;
            serviceStatus = GrpcUserCachingResponseStatus.fromCode(response.getStatus());
            data = new GrpcUserCachingResponseData(response);

        } catch (io.grpc.StatusRuntimeException e) {
            serverStatus = GrpcExceptionHandler.mapToGrpcRuntimeStatus(e);
            serviceStatus = GrpcUserCachingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;

            String errorLogMessage = String.format("%sgRPC call failed due to %s server unavailability or host resolution issue: [Status: %s, Description: \"%s\"]",
                    cahceServerStub.getLogPrefix(), cahceServerStub.getLayerName(), e.getStatus().getCode(), e.getStatus().getDescription());

            log.error(errorLogMessage);

        } catch (Exception e) {
            serverStatus = GrpcRuntimeStatus.INTERNAL_SERVER_ERROR;
            serviceStatus = GrpcUserCachingResponseStatus.BUSINESS_LOGIC_ERROR;
            data = null;
        }

        result.setGrpcServerStatus(serverStatus);
        result.setGrpcResponseStatus(serviceStatus);
        result.setGrpcResponseData(data);

        return result;
    }
}
