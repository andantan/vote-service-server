package org.zerock.voteservice.adapter.out.grpc.stub;

import lombok.extern.log4j.Log4j2;

import domain.vote.submit.protocol.BlockchainVoteSubmitServiceGrpc;
import domain.vote.submit.protocol.SubmitBallotTransactionRequest;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.out.grpc.common.AbstractGrpcClientStub;


@Log4j2
@Component
public class BallotTransactionServiceGrpcStub extends AbstractGrpcClientStub {

    private final BlockchainVoteSubmitServiceGrpc.BlockchainVoteSubmitServiceBlockingStub stub;

    public BallotTransactionServiceGrpcStub(
            @Value("${grpc.server.vote.submit.host}") String host,
            @Value("${grpc.server.vote.submit.port}") int port
    ) {
        super("L4", BlockchainVoteSubmitServiceGrpc.class.getSimpleName(), host, port);

        this.stub = BlockchainVoteSubmitServiceGrpc.newBlockingStub(channel);
    }

    public SubmitBallotTransactionResponse submitBallotTransaction(
            String userHash, String topic, String option
    ) throws RuntimeException {
        SubmitBallotTransactionRequest request = SubmitBallotTransactionRequest.newBuilder()
                .setUserHash(userHash)
                .setOption(option)
                .setTopic(topic)
                .build();

        return stub.submitBallotTransaction(request);
    }
}
