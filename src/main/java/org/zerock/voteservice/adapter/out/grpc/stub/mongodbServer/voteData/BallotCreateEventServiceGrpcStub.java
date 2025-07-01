package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.create.protocol.BallotCreateEventServiceGrpc;
import domain.event.ballot.create.protocol.BallotValidateEventRequest;
import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.event.ballot.create.protocol.BallotCacheEventRequest;
import domain.event.ballot.create.protocol.BallotCacheEventResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class BallotCreateEventServiceGrpcStub {
    private static final String SERVICE_NAME = BallotCreateEventServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L3";

    private final BallotCreateEventServiceGrpc.BallotCreateEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public BallotCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.ballot.create.host}") String host,
            @Value("${grpc.server.event.ballot.create.port}") int port
    ) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        stub = BallotCreateEventServiceGrpc.newBlockingStub(channel);
    }

    public BallotValidateEventResponse validateBallot(
            String userHash, String topic, String option
    ) throws RuntimeException {
        BallotValidateEventRequest request = BallotValidateEventRequest.newBuilder()
                .setUserHash(userHash)
                .setTopic(topic)
                .setOption(option)
                .build();

        try {
            return stub.validateBallotEvent(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }

    public BallotCacheEventResponse cacheBallot(
            String userHash, String voteHash, String topic
    ) throws RuntimeException {
        BallotCacheEventRequest request = BallotCacheEventRequest.newBuilder()
                .setUserHash(userHash)
                .setVoteHash(voteHash)
                .setTopic(topic)
                .build();

        try {
            return stub.cacheBallotEvent(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
