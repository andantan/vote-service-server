package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.event.proposal.create.protocol.ProposalCreateEventServiceGrpc;
import domain.event.proposal.create.protocol.ProposalValidateEventRequest;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventRequest;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;

import java.util.List;

@Log4j2
@Component
public class GrpcProposalCreateEventServiceStub extends AbstractGrpcClientStub {
    private final ProposalCreateEventServiceGrpc.ProposalCreateEventServiceBlockingStub stub;

    public GrpcProposalCreateEventServiceStub(
            @Value("${grpc.server.event.proposal.create.host}") String host,
            @Value("${grpc.server.event.proposal.create.port}") int port
    ) {
        super("L3", ProposalCreateEventServiceGrpc.class.getSimpleName(), host, port);

        stub = ProposalCreateEventServiceGrpc.newBlockingStub(channel);
    }

    public ProposalValidateEventResponse validateProposal(String topic) throws RuntimeException {
        ProposalValidateEventRequest request = ProposalValidateEventRequest.newBuilder()
                .setTopic(topic)
                .build();

        return stub.validateProposalEvent(request);
    }

    public ProposalCacheEventResponse cacheProposal(
            String topic, int duration, List<String> options
    ) throws RuntimeException {
        ProposalCacheEventRequest request = ProposalCacheEventRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .addAllOptions(options)
                .build();

        return stub.cacheProposalEvent(request);
    }
}
