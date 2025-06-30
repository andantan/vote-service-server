package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.userData;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import domain.event.user.create.protocol.UserCreateEventServiceGrpc;
import domain.event.user.create.protocol.UserValidateEventRequest;
import domain.event.user.create.protocol.UserValidateEventResponse;
import domain.event.user.create.protocol.UserCacheEventRequest;
import domain.event.user.create.protocol.UserCacheEventResponse;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
@Service
public class UserCreateEventServiceGrpcStub {
    private final UserCreateEventServiceGrpc.UserCreateEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public UserCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.user.create.host}") String grpcUserCreateEventConnectionHost,
            @Value("${grpc.server.event.user.create.port}") int grpcUserCreateEventConnectionPort
    ) {
        this.grpcHost = grpcUserCreateEventConnectionHost;
        this.grpcPort = grpcUserCreateEventConnectionPort;

        log.debug("[UserCreateEventServiceGrpcStub] Attempting to connect to gRPC server at {}:{}", grpcHost, grpcPort);

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcUserCreateEventConnectionHost, grpcUserCreateEventConnectionPort)
                .usePlaintext()
                .build();

        stub = UserCreateEventServiceGrpc.newBlockingStub(channel);

        log.debug("[UserCreateEventServiceGrpcStub] gRPC channel for ProposalQueryEventService initialized.");
    }

    public UserCacheEventResponse cacheUser(
            Integer uid, String userHash, String gender, Timestamp birthDate
    ) {
        UserCacheEventRequest request = UserCacheEventRequest.newBuilder()
                .setUid(uid)
                .setUserHash(userHash)
                .setGender(gender)
                .setBirthDate(birthDate)
                .build();

        return stub.cacheUserEvent(request);
    }

    public UserValidateEventResponse validateUser(
            Integer uid, String userHash
    ) {
        UserValidateEventRequest request = UserValidateEventRequest.newBuilder()
                .setUid(uid)
                .setUserHash(userHash)
                .build();

        try {
            return stub.validateUserEvent(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                String unavailableServer = "L3";
                String errorMessage = String.format("gRPC call to %s failed due to server unavailability or host resolution issue: %s:%d [%s]", unavailableServer, grpcHost, grpcPort, e);
                throw new RuntimeException(errorMessage);
            }
            log.error("gRPC call to UserCreateEventService for validateUser failed with status: {} (Description: {}). Request: {}",
                    e.getStatus().getCode(), e.getStatus().getDescription(), request, e);

            throw e;
        }
    }
}
