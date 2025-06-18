package org.zerock.voteservice.grpc.event;

import domain.event.ballot.query.protocol.BallotQueryEventServiceGrpc;
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
public class GrpcProposalQueryEventClient {

    private final ProposalQueryEventServiceGrpc.ProposalQueryEventServiceBlockingStub stub;

    public GrpcProposalQueryEventClient(
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
