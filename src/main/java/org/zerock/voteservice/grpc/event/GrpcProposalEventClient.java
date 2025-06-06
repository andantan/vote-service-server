package org.zerock.voteservice.grpc.event;

import domain.event.proposal.protocol.NewProposalEventServiceGrpc;
import domain.event.proposal.protocol.NewProposalEvent;
import domain.event.proposal.protocol.ValidateProposalEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class GrpcProposalEventClient {
    private final NewProposalEventServiceGrpc.NewProposalEventServiceBlockingStub stub;

    public GrpcProposalEventClient(String grpcProposalEventConnectionHost, int grpcProposalEventConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcProposalEventConnectionHost, grpcProposalEventConnectionPort)
                .usePlaintext()
                .build();

        stub = NewProposalEventServiceGrpc.newBlockingStub(channel);
    }

    public Map<String, Object> validateNewProposalEvent(String topic, int duration) {
        log.info("#[gRPC]#[To  : MongoDB-Cache-Server] ValidateNewProposalEvent request: topic='{}', duration={}",
                topic, duration
        );

        NewProposalEvent request = NewProposalEvent.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        ValidateProposalEventResponse response = stub.validateNewProposalEvent(request);

        log.info("#[gRPC]#[From: MongoDB-Cache-Server] ValidateNewProposalEvent response: Success={}, Message='{}'",
                response.getSuccess(),
                response.getMessage()
        );

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("success", response.getSuccess());
        responseMap.put("message", response.getMessage());

        return responseMap;
    }
}
