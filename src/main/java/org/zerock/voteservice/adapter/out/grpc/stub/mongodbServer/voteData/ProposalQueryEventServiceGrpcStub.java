package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import domain.event.proposal.query.protocol.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ProposalQueryEventServiceGrpcStub {

    private final ProposalQueryEventServiceGrpc.ProposalQueryEventServiceBlockingStub stub;

    public ProposalQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.proposal.query.host}") String grpcProposalQueryEventConnectionHost,
            @Value("${grpc.server.event.proposal.query.port}") int grpcProposalQueryEventConnectionPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcProposalQueryEventConnectionHost, grpcProposalQueryEventConnectionPort)
                .usePlaintext()
                .build();

        stub = ProposalQueryEventServiceGrpc.newBlockingStub(channel);
    }

    public GetProposalDetailResponse getProposalDetail(String topic) {
        GetProposalDetailRequest request = GetProposalDetailRequest.newBuilder()
                .setTopic(topic)
                .build();

        return stub.getProposalDetail(request);
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
