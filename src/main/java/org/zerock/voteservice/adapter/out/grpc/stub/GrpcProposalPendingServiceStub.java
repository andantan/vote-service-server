package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.vote.proposal.protocol.BlockchainVoteProposalServiceGrpc;
import domain.vote.proposal.protocol.OpenProposalPendingRequest;
import domain.vote.proposal.protocol.OpenProposalPendingResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;

@Log4j2
@Component
public class GrpcProposalPendingServiceStub extends AbstractGrpcClientStub {

    private final BlockchainVoteProposalServiceGrpc.BlockchainVoteProposalServiceBlockingStub stub;

    public GrpcProposalPendingServiceStub(
            @Value("${grpc.server.vote.proposal.host}") String host,
            @Value("${grpc.server.vote.proposal.port}") int port
    ) {
        super("L4", BlockchainVoteProposalServiceGrpc.class.getSimpleName(), host, port);

        stub = BlockchainVoteProposalServiceGrpc.newBlockingStub(channel);
    }

    public OpenProposalPendingResponse openProposalPending(
            String topic, int duration
    ) throws RuntimeException {
        OpenProposalPendingRequest request = OpenProposalPendingRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        return stub.openProposalPending(request);
    }
}
