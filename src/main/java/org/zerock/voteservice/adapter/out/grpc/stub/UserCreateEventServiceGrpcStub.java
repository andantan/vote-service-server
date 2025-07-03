package org.zerock.voteservice.adapter.out.grpc.stub;

import com.google.protobuf.Timestamp;

import domain.event.user.create.protocol.UserCreateEventServiceGrpc;
import domain.event.user.create.protocol.UserValidateEventRequest;
import domain.event.user.create.protocol.UserValidateEventResponse;
import domain.event.user.create.protocol.UserCacheEventRequest;
import domain.event.user.create.protocol.UserCacheEventResponse;

import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.zerock.voteservice.adapter.out.grpc.stub.common.AbstractGrpcClientStub;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;


@Log4j2
@Service
public class UserCreateEventServiceGrpcStub extends AbstractGrpcClientStub {
    private final UserCreateEventServiceGrpc.UserCreateEventServiceBlockingStub stub;

    public UserCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.user.create.host}") String host,
            @Value("${grpc.server.event.user.create.port}") int port
    ) {
        super("L3", UserCreateEventServiceGrpc.class.getSimpleName(), host, port);

        stub = UserCreateEventServiceGrpc.newBlockingStub(channel);
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
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
            );
        }
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
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
