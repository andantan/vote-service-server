package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import domain.event.proposal.create.protocol.ProposalCreateEventServiceGrpc;
import domain.event.proposal.create.protocol.ProposalValidateEventRequest;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventRequest;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Log4j2
@Service
public class ProposalCreateEventServiceGrpcStub {
    private final ProposalCreateEventServiceGrpc.ProposalCreateEventServiceBlockingStub stub;

    public ProposalCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.proposal.create.host}") String grpcProposalCreateEventConnectionHost,
            @Value("${grpc.server.event.proposal.create.port}") int grpcProposalCreateEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcProposalCreateEventConnectionHost, grpcProposalCreateEventConnectionPort)
                .usePlaintext()
                .build();

        stub = ProposalCreateEventServiceGrpc.newBlockingStub(channel);
    }

    public ProposalValidateEventResponse validateProposal(String topic) {
        ProposalValidateEventRequest request = ProposalValidateEventRequest.newBuilder()
                .setTopic(topic)
                .build();

        return stub.validateProposalEvent(request);
    }

    public ProposalCacheEventResponse cacheProposal(String topic, int duration, List<String> options) {
        ProposalCacheEventRequest request = ProposalCacheEventRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .addAllOptions(options)
                .build();

        return stub.cacheProposalEvent(request);
    }
}
