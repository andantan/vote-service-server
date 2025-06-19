package org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import domain.event.proposal.query.protocol.ProposalQueryEventServiceGrpc;
import domain.event.proposal.query.protocol.GetProposalRequest;
import domain.event.proposal.query.protocol.GetProposalResponse;

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

    public GetProposalResponse getProposal(String topic) {
        GetProposalRequest request = GetProposalRequest.newBuilder()
                .setTopic(topic)
                .build();

        return stub.getProposal(request);
    }
}
