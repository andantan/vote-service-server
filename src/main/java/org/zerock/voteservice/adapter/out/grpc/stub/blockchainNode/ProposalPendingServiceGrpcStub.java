package org.zerock.voteservice.adapter.out.grpc.stub.blockchainNode;

import domain.vote.proposal.protocol.BlockchainVoteProposalServiceGrpc;
import domain.vote.proposal.protocol.OpenProposalPendingRequest;
import domain.vote.proposal.protocol.OpenProposalPendingResponse;

import io.grpc.ManagedChannel;

import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class ProposalPendingServiceGrpcStub {
    private static final String SERVICE_NAME = BlockchainVoteProposalServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L4";

    private final BlockchainVoteProposalServiceGrpc.BlockchainVoteProposalServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public ProposalPendingServiceGrpcStub(
            @Value("${grpc.server.vote.proposal.host}") String host,
            @Value("${grpc.server.vote.proposal.port}") int port
    ) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        stub = BlockchainVoteProposalServiceGrpc.newBlockingStub(channel);
    }

    public OpenProposalPendingResponse openProposalPending(
            String topic, int duration
    ) throws RuntimeException {
        OpenProposalPendingRequest request = OpenProposalPendingRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        try {
            return stub.openProposalPending(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
