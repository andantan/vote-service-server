package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.query.protocol.BallotQueryEventServiceGrpc;
import domain.event.ballot.query.protocol.GetUserBallotsRequest;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class BallotQueryEventServiceGrpcStub {
    private static final String SERVICE_NAME = BallotQueryEventServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L3";

    private final BallotQueryEventServiceGrpc.BallotQueryEventServiceBlockingStub stub;

    private final String grpcHost;
    private final int grpcPort;

    public BallotQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.ballot.query.host}") String host,
            @Value("${grpc.server.event.ballot.query.port}") int port
    ) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        stub = BallotQueryEventServiceGrpc.newBlockingStub(channel);
    }

    public GetUserBallotsResponse getUserBallots(String userHash) throws RuntimeException {
        GetUserBallotsRequest request = GetUserBallotsRequest.newBuilder()
                .setUserHash(userHash)
                .build();

        try {
            return stub.getUserBallots(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
