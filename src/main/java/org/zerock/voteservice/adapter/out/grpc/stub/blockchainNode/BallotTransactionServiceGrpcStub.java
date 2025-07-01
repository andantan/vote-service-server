package org.zerock.voteservice.adapter.out.grpc.stub.blockchainNode;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import domain.vote.submit.protocol.BlockchainVoteSubmitServiceGrpc;
import domain.vote.submit.protocol.SubmitBallotTransactionRequest;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class BallotTransactionServiceGrpcStub {
    private static final String SERVICE_NAME = BlockchainVoteSubmitServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L4";

    private final BlockchainVoteSubmitServiceGrpc.BlockchainVoteSubmitServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public BallotTransactionServiceGrpcStub(
            @Value("${grpc.server.vote.submit.host}") String host,
            @Value("${grpc.server.vote.submit.port}") int port
    ) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        stub = BlockchainVoteSubmitServiceGrpc.newBlockingStub(channel);
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
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
