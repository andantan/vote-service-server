package org.zerock.voteservice.grpc.event;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.protocol.NewBallotEventServiceGrpc;
import domain.event.ballot.protocol.ValidateBallotEventRequest;
import domain.event.ballot.protocol.ValidateBallotEventResponse;
import domain.event.ballot.protocol.CacheBallotEventRequest;
import domain.event.ballot.protocol.CacheBallotEventResponse;

@Log4j2
public class GrpcBallotEventClient {
    private final NewBallotEventServiceGrpc.NewBallotEventServiceBlockingStub stub;

    public GrpcBallotEventClient(String grpcBallotEventConnectionHost, int grpcBallotEventConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcBallotEventConnectionHost, grpcBallotEventConnectionPort)
                .usePlaintext()
                .build();

        stub = NewBallotEventServiceGrpc.newBlockingStub(channel);
    }

    public ValidateBallotEventResponse validateBallot(String userHash, String topic) {
        ValidateBallotEventRequest request = ValidateBallotEventRequest.newBuilder()
                .setUserHash(userHash)
                .setTopic(topic)
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
