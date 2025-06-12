package org.zerock.voteservice.grpc.vote;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.vote.submit.protocol.BlockchainVoteSubmitServiceGrpc;
import domain.vote.submit.protocol.SubmitBallotTransactionRequest;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

@Log4j2
public class GrpcBallotTransactionClient {
    private final BlockchainVoteSubmitServiceGrpc.BlockchainVoteSubmitServiceBlockingStub stub;

    public GrpcBallotTransactionClient(String grpcVoteConnectionHost, int grpcVoteConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcVoteConnectionHost, grpcVoteConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteSubmitServiceGrpc.newBlockingStub(channel);
    }

    public SubmitBallotTransactionResponse submitBallotTransaction(String userHash, String topic, String option) {
        SubmitBallotTransactionRequest request = SubmitBallotTransactionRequest.newBuilder()
                .setUserHash(userHash)
                .setOption(option)
                .setTopic(topic)
                .build();

        return stub.submitBallotTransaction(request);
    }
}
