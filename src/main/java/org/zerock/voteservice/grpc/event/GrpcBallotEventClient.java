package org.zerock.voteservice.grpc.event;

import domain.event.ballot.protocol.NewBallotEventServiceGrpc;
import domain.event.ballot.protocol.NewBallotEvent;
import domain.event.ballot.protocol.ValidateBallotEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> validateNewBallotEvent(String topic, String hash, String option) {
        log.info("#[gRPC]#[To  : MongoDB-Cache-Server] ValidateNewBallotEvent request: topic={}, hash={}, option={}",
                topic, hash, option
        );

        NewBallotEvent request = NewBallotEvent.newBuilder()
                .setTopic(topic)
                .setHash(hash)
                .setOption(option)
                .build();

        ValidateBallotEventResponse response = stub.validateNewBallotEvent(request);

        log.info("#[gRPC]#[From: MongoDB-Cache-Server] ValidateNewBallotEvent response: Success={}, Message={}",
                response.getSuccess(),
                response.getMessage()
        );

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("success", response.getSuccess());
        responseMap.put("message", response.getMessage());

        return responseMap;
    }
}
