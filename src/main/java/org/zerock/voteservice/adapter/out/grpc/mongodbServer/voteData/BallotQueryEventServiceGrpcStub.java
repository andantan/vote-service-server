package org.zerock.voteservice.adapter.out.grpc.mongodbServer.voteData;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.query.protocol.BallotQueryEventServiceGrpc;
import domain.event.ballot.query.protocol.GetUserBallotsRequest;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BallotQueryEventServiceGrpcStub {
    private final BallotQueryEventServiceGrpc.BallotQueryEventServiceBlockingStub stub;

    public BallotQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.ballot.query.host}") String grpcBallotCreateEventConnectionHost,
            @Value("${grpc.server.event.ballot.query.port}") int grpcBallotCreateEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcBallotCreateEventConnectionHost, grpcBallotCreateEventConnectionPort)
                .usePlaintext()
                .build();

        stub = BallotQueryEventServiceGrpc.newBlockingStub(channel);
    }

    public GetUserBallotsResponse getUserBallots(String userHash) {
        GetUserBallotsRequest request = GetUserBallotsRequest.newBuilder()
                .setUserHash(userHash)
                .build();

        return stub.getUserBallots(request);
    }
}
