package org.zerock.voteservice.adapter.out.grpc.stub;

import lombok.extern.log4j.Log4j2;

import domain.event.ballot.query.protocol.BallotQueryEventServiceGrpc;
import domain.event.ballot.query.protocol.GetUserBallotsRequest;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;


@Log4j2
@Component
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

        return stub.getUserBallots(request);
    }
}
