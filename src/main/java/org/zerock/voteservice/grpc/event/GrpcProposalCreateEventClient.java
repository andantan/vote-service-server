package org.zerock.voteservice.grpc.event;

import domain.event.proposal.create.protocol.NewProposalEventServiceGrpc;
import domain.event.proposal.create.protocol.ValidateProposalEventRequest;
import domain.event.proposal.create.protocol.ValidateProposalEventResponse;
import domain.event.proposal.create.protocol.CacheProposalEventRequest;
import domain.event.proposal.create.protocol.CacheProposalEventResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Log4j2
@Service
public class GrpcProposalCreateEventClient {
    private final NewProposalEventServiceGrpc.NewProposalEventServiceBlockingStub stub;

    public GrpcProposalCreateEventClient(
            @Value("${grpc.server.event.proposal.create.host}") String grpcProposalCreateEventConnectionHost,
            @Value("${grpc.server.event.proposal.create.port}") int grpcProposalCreateEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcProposalCreateEventConnectionHost, grpcProposalCreateEventConnectionPort)
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

    public CacheProposalEventResponse cacheProposal(String topic, int duration, List<String> options) {
        CacheProposalEventRequest request = CacheProposalEventRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .addAllOptions(options)
                .build();

        return stub.cacheNewProposalEvent(request);
    }
}
