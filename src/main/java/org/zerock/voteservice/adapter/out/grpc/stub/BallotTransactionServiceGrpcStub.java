package org.zerock.voteservice.adapter.out.grpc.stub;

import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import domain.vote.submit.protocol.BlockchainVoteSubmitServiceGrpc;
import domain.vote.submit.protocol.SubmitBallotTransactionRequest;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.stub.common.AbstractGrpcClientStub;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;


@Log4j2
@Service
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

        try {
            return stub.submitBallotTransaction(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
