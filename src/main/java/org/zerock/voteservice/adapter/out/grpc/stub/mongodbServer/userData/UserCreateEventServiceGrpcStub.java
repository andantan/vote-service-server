package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.userData;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import domain.event.user.create.protocol.UserCreateEventServiceGrpc;
import domain.event.user.create.protocol.UserCacheEventRequest;
import domain.event.user.create.protocol.UserCacheEventResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
@Service
public class UserCreateEventServiceGrpcStub {
    private final UserCreateEventServiceGrpc.UserCreateEventServiceBlockingStub stub;

    public UserCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.user.create.host}") String grpcUserCreateEventConnectionHost,
            @Value("${grpc.server.event.user.create.port}") int grpcUserCreateEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcUserCreateEventConnectionHost, grpcUserCreateEventConnectionPort)
                .usePlaintext()
                .build();

        stub = UserCreateEventServiceGrpc.newBlockingStub(channel);
    }

    public UserCacheEventResponse cacheUser(
            String userHash, Integer uid, String gender, Timestamp birthDate
    ) {
        UserCacheEventRequest request = UserCacheEventRequest.newBuilder()
                .setUserHash(userHash)
                .setUid(uid)
                .setGender(gender)
                .setBirthDate(birthDate)
                .build();

        return stub.cacheUserEvent(request);
    }
}
