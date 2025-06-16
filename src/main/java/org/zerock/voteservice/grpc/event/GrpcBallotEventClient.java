package org.zerock.voteservice.grpc.event;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.event.ballot.protocol.NewBallotEventServiceGrpc;
import domain.event.ballot.protocol.ValidateBallotEventRequest;
import domain.event.ballot.protocol.ValidateBallotEventResponse;
import domain.event.ballot.protocol.CacheBallotEventRequest;
import domain.event.ballot.protocol.CacheBallotEventResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class GrpcBallotEventClient {
    private final NewBallotEventServiceGrpc.NewBallotEventServiceBlockingStub stub;

    public GrpcBallotEventClient(
            @Value("${grpc.server.event.ballot.host}") String grpcBallotEventConnectionHost,
            @Value("${grpc.server.event.ballot.port}") int grpcBallotEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcBallotEventConnectionHost, grpcBallotEventConnectionPort)
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
