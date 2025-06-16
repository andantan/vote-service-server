package org.zerock.voteservice.grpc.vote;

import domain.vote.proposal.protocol.BlockchainVoteProposalServiceGrpc;
import domain.vote.proposal.protocol.OpenProposalPendingRequest;
import domain.vote.proposal.protocol.OpenProposalPendingResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
@Service
public class GrpcProposalPendingClient {
    private final BlockchainVoteProposalServiceGrpc.BlockchainVoteProposalServiceBlockingStub stub;

    public GrpcProposalPendingClient(
            @Value("${grpc.server.vote.proposal.host}") String grpcTopicConnectionHost,
            @Value("${grpc.server.vote.proposal.port}") int grpcTopicConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcTopicConnectionHost, grpcTopicConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteProposalServiceGrpc.newBlockingStub(channel);
    }

    public OpenProposalPendingResponse openProposalPending(String topic, int duration) {
        OpenProposalPendingRequest request = OpenProposalPendingRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        return stub.openProposalPending(request);
    }
}
