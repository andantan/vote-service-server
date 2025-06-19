package org.zerock.voteservice.adapter.in.web.controller.vote;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.vote.docs.VoteProposalApiDoc;

import org.zerock.voteservice.adapter.in.web.mapper.VoteApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.adapter.in.web.controller.vote.processor.ProposalCreateProcessor;

@Log4j2
@RestController
public class VoteProposalApiController extends VoteApiEndpointMapper {
    private final ProposalCreateProcessor proposalCreateProcessor;

    public VoteProposalApiController(ProposalCreateProcessor proposalCreateProcessor) {
        this.proposalCreateProcessor = proposalCreateProcessor;
    }

    @VoteProposalApiDoc
    @PostMapping("/proposal")
    public ResponseEntity<? extends ResponseDto> proposalVote(@RequestBody VoteProposalRequestDto dto) {
        // Cache server: request validate proposal [gRPC]
        ProposalValidateEventResponse validatedProposal = this.proposalCreateProcessor.validateProposal(dto);

        if (!validatedProposal.getValidation()) {
            return this.proposalCreateProcessor.getErrorResponse(validatedProposal.getStatus());
        }

        // Blockchain server: request open pending [gRPC]
        OpenProposalPendingResponse pendedProposal = this.proposalCreateProcessor.requestOpenPending(dto);

        if (!pendedProposal.getSuccess()) {
            return this.proposalCreateProcessor.getErrorResponse(pendedProposal.getStatus());
        }

        // Cache server: request cache proposal [gRPC]
        ProposalCacheEventResponse cachedProposal = this.proposalCreateProcessor.requestCacheProposal(dto);

        if (!cachedProposal.getCached()) {
            return this.proposalCreateProcessor.getErrorResponse(cachedProposal.getStatus());
        }

        // All steps passed
        return this.proposalCreateProcessor.getSuccessResponse(dto, cachedProposal.getStatus());
    }
}
