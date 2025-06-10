package org.zerock.voteservice.grpc.event;

import domain.event.proposal.protocol.NewProposalEventServiceGrpc;
import domain.event.proposal.protocol.ValidateProposalEventRequest;
import domain.event.proposal.protocol.ValidateProposalEventResponse;
import domain.event.proposal.protocol.CacheProposalEventRequest;
import domain.event.proposal.protocol.CacheProposalEventResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

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

    public ValidateProposalEventResponse validateProposal(String topic) {
        ValidateProposalEventRequest request = ValidateProposalEventRequest.newBuilder()
                .setTopic(topic)
                .build();

        return stub.validateNewProposalEvent(request);
    }

    public CacheProposalEventResponse cacheProposal(String topic, int duration) {
        CacheProposalEventRequest request = CacheProposalEventRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        return stub.cacheNewProposalEvent(request);
    }
}
