package org.zerock.voteservice.controller.vote;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import domain.event.proposal.create.protocol.CacheProposalEventResponse;
import domain.event.proposal.create.protocol.ValidateProposalEventResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.controller.vote.docs.VoteProposalApiDoc;

import org.zerock.voteservice.controller.mapper.ApiVoteEndpointMapper;
import org.zerock.voteservice.dto.ResponseDto;
import org.zerock.voteservice.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.controller.vote.processor.ProposalProcessor;

@Log4j2
@RestController
public class ApiVoteProposalController extends ApiVoteEndpointMapper {
    private final ProposalProcessor processor;

    public ApiVoteProposalController(ProposalProcessor processor) {
        this.processor = processor;
    }

    @VoteProposalApiDoc
    @PostMapping("/proposal")
    public ResponseEntity<? extends ResponseDto> proposalVote(@RequestBody VoteProposalRequestDto dto) {
        // Cache server: request validate proposal [gRPC]
        ValidateProposalEventResponse validatedProposal = this.processor.validateProposal(dto);

        if (!validatedProposal.getValidation()) {
            return this.processor.getErrorResponse(validatedProposal.getStatus());
        }

        // Blockchain server: request open pending [gRPC]
        OpenProposalPendingResponse pendedProposal = this.processor.requestOpenPending(dto);

        if (!pendedProposal.getSuccess()) {
            return this.processor.getErrorResponse(pendedProposal.getStatus());
        }

        // Cache server: request cache proposal [gRPC]
        CacheProposalEventResponse cachedProposal = this.processor.requestCacheProposal(dto);

        if (!cachedProposal.getCached()) {
            return this.processor.getErrorResponse(cachedProposal.getStatus());
        }

        // All steps passed
        return this.processor.getSuccessResponse(dto, cachedProposal.getStatus());
    }
}
