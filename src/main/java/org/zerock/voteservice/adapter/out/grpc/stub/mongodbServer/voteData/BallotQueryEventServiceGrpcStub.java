package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.query.protocol.BallotQueryEventServiceGrpc;
import domain.event.ballot.query.protocol.GetUserBallotsRequest;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.stub.common.AbstractGrpcClientStub;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;


@Log4j2
@Service
public class BallotQueryEventServiceGrpcStub extends AbstractGrpcClientStub {
    private final BallotQueryEventServiceGrpc.BallotQueryEventServiceBlockingStub stub;

    public BallotQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.ballot.query.host}") String host,
            @Value("${grpc.server.event.ballot.query.port}") int port
    ) {
        super("L3", BallotQueryEventServiceGrpc.class.getSimpleName(), host, port);

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
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
