package org.zerock.voteservice.adapter.out.grpc.stub;

import domain.vote.proposal.protocol.BlockchainVoteProposalServiceGrpc;
import domain.vote.proposal.protocol.OpenProposalPendingRequest;
import domain.vote.proposal.protocol.OpenProposalPendingResponse;

import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.zerock.voteservice.adapter.out.grpc.stub.common.AbstractGrpcClientStub;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class ProposalPendingServiceGrpcStub extends AbstractGrpcClientStub {

    private final BlockchainVoteProposalServiceGrpc.BlockchainVoteProposalServiceBlockingStub stub;

    public ProposalPendingServiceGrpcStub(
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

        try {
            return stub.openProposalPending(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
