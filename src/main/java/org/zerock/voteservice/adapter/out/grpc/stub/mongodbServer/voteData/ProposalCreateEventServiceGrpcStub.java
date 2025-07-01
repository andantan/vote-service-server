package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import domain.event.proposal.create.protocol.ProposalCreateEventServiceGrpc;
import domain.event.proposal.create.protocol.ProposalValidateEventRequest;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventRequest;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

import java.util.List;

@Log4j2
@Service
public class ProposalCreateEventServiceGrpcStub {
    private static final String SERVICE_NAME = ProposalCreateEventServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L3";

    private final ProposalCreateEventServiceGrpc.ProposalCreateEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public ProposalCreateEventServiceGrpcStub(
            @Value("${grpc.server.event.proposal.create.host}") String host,
            @Value("${grpc.server.event.proposal.create.port}") int port
    ) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        stub = ProposalCreateEventServiceGrpc.newBlockingStub(channel);
    }

    public ProposalValidateEventResponse validateProposal(String topic) throws RuntimeException {
        ProposalValidateEventRequest request = ProposalValidateEventRequest.newBuilder()
                .setTopic(topic)
                .build();

        try {
            return stub.validateProposalEvent(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }

    public ProposalCacheEventResponse cacheProposal(
            String topic, int duration, List<String> options
    ) throws RuntimeException {
        ProposalCacheEventRequest request = ProposalCacheEventRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .addAllOptions(options)
                .build();

        try {
            return stub.cacheProposalEvent(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
