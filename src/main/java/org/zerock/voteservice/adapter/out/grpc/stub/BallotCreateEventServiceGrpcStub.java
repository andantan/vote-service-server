package org.zerock.voteservice.adapter.out.grpc.stub;

import lombok.extern.log4j.Log4j2;

import domain.event.ballot.create.protocol.BallotCreateEventServiceGrpc;
import domain.event.ballot.create.protocol.BallotValidateEventRequest;
import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.event.ballot.create.protocol.BallotCacheEventRequest;
import domain.event.ballot.create.protocol.BallotCacheEventResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;

@Log4j2
@Service
public class BallotCreateEventServiceGrpcStub extends AbstractGrpcClientStub {
    private final BallotCreateEventServiceGrpc.BallotCreateEventServiceBlockingStub stub;

    public BallotCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.ballot.create.host}") String host,
            @Value("${grpc.server.event.ballot.create.port}") int port
    ) {
        super("L3", BallotCreateEventServiceGrpc.class.getSimpleName(), host, port);

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

        return stub.validateBallotEvent(request);
    }

    public BallotCacheEventResponse cacheBallot(
            String userHash, String voteHash, String topic
    ) throws RuntimeException {
        BallotCacheEventRequest request = BallotCacheEventRequest.newBuilder()
                .setUserHash(userHash)
                .setVoteHash(voteHash)
                .setTopic(topic)
                .build();

        return stub.cacheBallotEvent(request);
    }
}
