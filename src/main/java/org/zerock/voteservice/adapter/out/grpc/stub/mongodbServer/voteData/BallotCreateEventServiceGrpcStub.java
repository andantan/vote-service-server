package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.create.protocol.BallotCreateEventServiceGrpc;
import domain.event.ballot.create.protocol.BallotValidateEventRequest;
import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.event.ballot.create.protocol.BallotCacheEventRequest;
import domain.event.ballot.create.protocol.BallotCacheEventResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BallotCreateEventServiceGrpcStub {
    private final BallotCreateEventServiceGrpc.BallotCreateEventServiceBlockingStub stub;

    public BallotCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.ballot.create.host}") String grpcBallotCreateEventConnectionHost,
            @Value("${grpc.server.event.ballot.create.port}") int grpcBallotCreateEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcBallotCreateEventConnectionHost, grpcBallotCreateEventConnectionPort)
                .usePlaintext()
                .build();

        stub = BallotCreateEventServiceGrpc.newBlockingStub(channel);
    }

    public BallotValidateEventResponse validateBallot(String userHash, String topic, String option) {
        BallotValidateEventRequest request = BallotValidateEventRequest.newBuilder()
                .setUserHash(userHash)
                .setTopic(topic)
                .setOption(option)
                .build();

        return stub.validateBallotEvent(request);
    }

    public BallotCacheEventResponse cacheBallot(String userHash, String voteHash, String topic) {
        BallotCacheEventRequest request = BallotCacheEventRequest.newBuilder()
                .setUserHash(userHash)
                .setVoteHash(voteHash)
                .setTopic(topic)
                .build();

        return stub.cacheBallotEvent(request);
    }
}
