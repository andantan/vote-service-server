package org.zerock.voteservice.experiment.out.stub;

import domain.event.proposal.query.protocol.GetProposalDetailRequest;
import domain.event.proposal.query.protocol.GetProposalDetailResponse;
import domain.event.proposal.query.protocol.ProposalQueryEventServiceGrpc;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.out.grpc.stub.common.AbstractGrpcClientStub;

@Log4j2
@Component
public class ExperimentProposalQueryEventServiceGrpcStub extends AbstractGrpcClientStub {
    private final ProposalQueryEventServiceGrpc.ProposalQueryEventServiceBlockingStub stub;

    public ExperimentProposalQueryEventServiceGrpcStub(
            @Value("${grpc.server.event.proposal.query.host}") String host,
            @Value("${grpc.server.event.proposal.query.port}") int port
    ) {
        super("L3-Experiment", ProposalQueryEventServiceGrpc.class.getSimpleName(), host, port);

        this.stub = ProposalQueryEventServiceGrpc.newBlockingStub(channel);
        this.setLogPrefix("[ Experiment ] " + logPrefix);
    }

    public GetProposalDetailResponse getProposalDetail(String topic) {
        GetProposalDetailRequest request = GetProposalDetailRequest.newBuilder()
                .setTopic(topic)
                .build();

        return this.stub.getProposalDetail(request);
    }
}
