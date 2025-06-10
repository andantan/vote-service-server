package org.zerock.voteservice.grpc.vote;

import domain.vote.proposal.protocol.BlockchainVoteProposalServiceGrpc;
import domain.vote.proposal.protocol.OpenProposalRequest;
import domain.vote.proposal.protocol.OpenProposalResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GrpcProposalPendingClient {
    private final BlockchainVoteProposalServiceGrpc.BlockchainVoteProposalServiceBlockingStub stub;

    public GrpcProposalPendingClient(String grpcTopicConnectionHost, int grpcTopicConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcTopicConnectionHost, grpcTopicConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteProposalServiceGrpc.newBlockingStub(channel);
    }

    public OpenProposalResponse openProposalPending(String topic, int duration) {
        OpenProposalRequest request = OpenProposalRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        return stub.openProposalPending(request);
    }
}
