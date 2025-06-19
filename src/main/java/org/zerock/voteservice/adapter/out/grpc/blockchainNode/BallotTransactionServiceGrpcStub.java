package org.zerock.voteservice.adapter.out.grpc.blockchainNode;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.vote.submit.protocol.BlockchainVoteSubmitServiceGrpc;
import domain.vote.submit.protocol.SubmitBallotTransactionRequest;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BallotTransactionServiceGrpcStub {
    private final BlockchainVoteSubmitServiceGrpc.BlockchainVoteSubmitServiceBlockingStub stub;

    public BallotTransactionServiceGrpcStub(
            @Value("${grpc.server.vote.submit.host}") String grpcVoteConnectionHost,
            @Value("${grpc.server.vote.submit.port}") int grpcVoteConnectionPort
    ) {
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
