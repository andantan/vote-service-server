package org.zerock.voteservice.adapter.out.grpc.stub;

import lombok.extern.log4j.Log4j2;
import io.grpc.StatusRuntimeException;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import domain.event.proposal.query.protocol.*;
import org.zerock.voteservice.adapter.out.grpc.stub.common.AbstractGrpcClientStub;
import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;

@Log4j2
@Service
public class ProposalQueryEventServiceGrpcStub extends AbstractGrpcClientStub {
    private final ProposalQueryEventServiceGrpc.ProposalQueryEventServiceBlockingStub stub;

    public ProposalQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.proposal.query.host}") String host,
            @Value("${grpc.server.event.proposal.query.port}") int port
    ) {
        super("L3", ProposalQueryEventServiceGrpc.class.getSimpleName(), host, port);

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
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
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
                    e, layerName, serviceName, rpcName, grpcHost, grpcPort, request
            );
        }
    }
}
