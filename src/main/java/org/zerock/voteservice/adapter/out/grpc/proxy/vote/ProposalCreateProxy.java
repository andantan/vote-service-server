package org.zerock.voteservice.adapter.out.grpc.proxy.vote;

import domain.event.proposal.create.protocol.ProposalCacheEventResponse;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.adapter.out.grpc.stub.blockchainNode.ProposalPendingServiceGrpcStub;
import org.zerock.voteservice.adapter.out.grpc.stub.mongodbServer.voteData.ProposalCreateEventServiceGrpcStub;

@Log4j2
@Service
public class ProposalCreateProxy {
    private final ProposalCreateEventServiceGrpcStub proposalCreateEventServiceGrpcStub;
    private final ProposalPendingServiceGrpcStub proposalPendingServiceGrpcStub;

    public ProposalCreateProxy(
            ProposalPendingServiceGrpcStub proposalPendingServiceGrpcStub,
            ProposalCreateEventServiceGrpcStub proposalCreateEventServiceGrpcStub
    ) {
        this.proposalCreateEventServiceGrpcStub = proposalCreateEventServiceGrpcStub;
        this.proposalPendingServiceGrpcStub = proposalPendingServiceGrpcStub;
    }

    public ProposalValidateEventResponse validateProposal(VoteProposalRequestDto dto) {
        return this.proposalCreateEventServiceGrpcStub.validateProposal(dto.getTopic());
    }

    public OpenProposalPendingResponse requestOpenPending(VoteProposalRequestDto dto) {
        return this.proposalPendingServiceGrpcStub.openProposalPending(dto.getTopic(), dto.getDuration());
    }

    public ProposalCacheEventResponse requestCacheProposal(VoteProposalRequestDto dto) {
        return this.proposalCreateEventServiceGrpcStub.cacheProposal(dto.getTopic(), dto.getDuration(), dto.getOptions());
    }
}
