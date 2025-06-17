package org.zerock.voteservice.grpc.event;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.create.protocol.NewBallotEventServiceGrpc;
import domain.event.ballot.create.protocol.ValidateBallotEventRequest;
import domain.event.ballot.create.protocol.ValidateBallotEventResponse;
import domain.event.ballot.create.protocol.CacheBallotEventRequest;
import domain.event.ballot.create.protocol.CacheBallotEventResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class GrpcBallotCreateEventClient {
    private final NewBallotEventServiceGrpc.NewBallotEventServiceBlockingStub stub;

    public GrpcBallotCreateEventClient(
            @Value("${grpc.server.event.ballot.create.host}") String grpcBallotCreateEventConnectionHost,
            @Value("${grpc.server.event.ballot.create.port}") int grpcBallotCreateEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcBallotCreateEventConnectionHost, grpcBallotCreateEventConnectionPort)
                .usePlaintext()
                .build();

        stub = NewBallotEventServiceGrpc.newBlockingStub(channel);
    }

    public ValidateBallotEventResponse validateBallot(String userHash, String topic, String option) {
        ValidateBallotEventRequest request = ValidateBallotEventRequest.newBuilder()
                .setUserHash(userHash)
                .setTopic(topic)
                .setOption(option)
                .build();

        return stub.validateNewBallotEvent(request);
    }

    public CacheBallotEventResponse cacheBallot(String userHash, String voteHash, String topic, String option) {
        CacheBallotEventRequest request = CacheBallotEventRequest.newBuilder()
                .setUserHash(userHash)
                .setVoteHash(voteHash)
                .setTopic(topic)
                .setOption(option)
                .build();

        return stub.cacheNewBallotEvent(request);
    }
}
