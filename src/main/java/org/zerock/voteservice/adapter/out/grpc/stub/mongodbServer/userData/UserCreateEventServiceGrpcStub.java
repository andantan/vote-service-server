package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.userData;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;

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
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class UserCreateEventServiceGrpcStub {
    private static final String SERVICE_NAME = UserCreateEventServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L3";

    private final UserCreateEventServiceGrpc.UserCreateEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public UserCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.user.create.host}") String host,
            @Value("${grpc.server.event.user.create.port}") int port
    ) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        stub = UserCreateEventServiceGrpc.newBlockingStub(channel);
    }

    public UserCacheEventResponse cacheUser(
            Integer uid, String userHash, String gender, Timestamp birthDate
    ) throws RuntimeException {
        UserCacheEventRequest request = UserCacheEventRequest.newBuilder()
                .setUid(uid)
                .setUserHash(userHash)
                .setGender(gender)
                .setBirthDate(birthDate)
                .build();

        try {
            return stub.cacheUserEvent(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }

    public UserValidateEventResponse validateUser(
            Integer uid, String userHash
    ) throws RuntimeException {
        UserValidateEventRequest request = UserValidateEventRequest.newBuilder()
                .setUid(uid)
                .setUserHash(userHash)
                .build();

        try {
            return stub.validateUserEvent(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
