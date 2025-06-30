package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import domain.event.proposal.query.protocol.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ProposalQueryEventServiceGrpcStub {

    private final ProposalQueryEventServiceGrpc.ProposalQueryEventServiceBlockingStub stub;
    private final String grpcHost;
    private final int grpcPort;

    public ProposalQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.proposal.query.host}") String grpcProposalQueryEventConnectionHost,
            @Value("${grpc.server.event.proposal.query.port}") int grpcProposalQueryEventConnectionPort
    ) {
        this.grpcHost = grpcProposalQueryEventConnectionHost;
        this.grpcPort = grpcProposalQueryEventConnectionPort;

        log.debug("Attempting to connect to gRPC server at {}:{}", grpcHost, grpcPort);

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();

        stub = ProposalQueryEventServiceGrpc.newBlockingStub(channel);

        log.debug("gRPC channel for ProposalQueryEventService initialized.");
    }

    public GetProposalDetailResponse getProposalDetail(String topic) {
        GetProposalDetailRequest request = GetProposalDetailRequest.newBuilder()
                .setTopic(topic)
                .build();

        try {
            return stub.getProposalDetail(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.UNAVAILABLE) {
                String unavailableServer = "L3";
                String errorMessage = String.format("gRPC call to %s failed due to server unavailability or host resolution issue: %s:%d [%s]", unavailableServer, grpcHost, grpcPort, e);
                throw new RuntimeException(errorMessage);
            }
            log.error("gRPC call to ProposalQueryEventService for getProposalDetail failed with status: {} (Description: {}). Request: {}",
                    e.getStatus().getCode(), e.getStatus().getDescription(), request, e);

            throw e;
        }
    }

    public GetFilteredProposalListResponse getFilteredProposalList(Filter filter, Sort sort, Paging paging) {
        GetFilteredProposalListRequest request = GetFilteredProposalListRequest.newBuilder()
                .setFilter(filter)
                .setSort(sort)
                .setPaging(paging)
                .build();

        return stub.getFilteredProposalList(request);
    }
}
