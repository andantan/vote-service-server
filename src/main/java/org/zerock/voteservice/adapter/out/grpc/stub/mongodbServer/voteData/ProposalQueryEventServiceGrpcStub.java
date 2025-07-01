package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import domain.event.proposal.query.protocol.*;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.common.GrpcChannelHandler;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class ProposalQueryEventServiceGrpcStub {
    private static final String SERVICE_NAME = ProposalQueryEventServiceGrpc.class.getSimpleName();
    private static final String LAYER_NAME = "L3";

    private final ProposalQueryEventServiceGrpc.ProposalQueryEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public ProposalQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.proposal.query.host}") String host,
            @Value("${grpc.server.event.proposal.query.port}") int port
    ) {
        this.grpcHost = host;
        this.grpcPort = port;

        ManagedChannel channel = GrpcChannelHandler.getPlainedManagedChannel(LAYER_NAME, SERVICE_NAME, host, port);

        this.stub = ProposalQueryEventServiceGrpc.newBlockingStub(channel);
    }

    public GetProposalDetailResponse getProposalDetail(String topic) throws RuntimeException {
        GetProposalDetailRequest request = GetProposalDetailRequest.newBuilder()
                .setTopic(topic)
                .build();

        try {
            return stub.getProposalDetail(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }

    public GetFilteredProposalListResponse getFilteredProposalList(
            Filter filter, Sort sort, Paging paging
    ) throws RuntimeException {
        GetFilteredProposalListRequest request = GetFilteredProposalListRequest.newBuilder()
                .setFilter(filter)
                .setSort(sort)
                .setPaging(paging)
                .build();

        try {
            return stub.getFilteredProposalList(request);
        } catch (StatusRuntimeException e) {
            String rpcName = Thread.currentThread().getStackTrace()[1].getMethodName();

            throw GrpcExceptionHandler.mapStatusRuntimeException(
                    e, LAYER_NAME, SERVICE_NAME, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
