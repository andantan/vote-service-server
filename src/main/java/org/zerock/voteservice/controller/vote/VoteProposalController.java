package org.zerock.voteservice.controller.vote;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import domain.event.proposal.protocol.CacheProposalEventResponse;
import domain.event.proposal.protocol.ValidateProposalEventResponse;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.controller.docs.VoteProposalApiDoc;
import org.zerock.voteservice.dto.vote.VoteProposalDto;
import org.zerock.voteservice.controller.vote.processor.VoteProposalProcessor;

import java.util.Map;

@Log4j2
@RestController
public class VoteProposalController extends VoteRequestMapper {
    private final VoteProposalProcessor voteProposalProcessor;

    public VoteProposalController(VoteProposalProcessor voteProposalProcessor) {
        this.voteProposalProcessor = voteProposalProcessor;
    }

    @VoteProposalApiDoc
    @PostMapping("/proposal")
    public Map<String, String> proposalVote(@RequestBody VoteProposalDto dto) {
        log.info("{}, {}", dto.getTopic(), dto.getDuration());

        // Cache server: request validate proposal [gRPC]
        ValidateProposalEventResponse validatedProposal = this.voteProposalProcessor.validateProposal(dto);

        if (!validatedProposal.getValidation()) {
            return this.voteProposalProcessor.getErrorResponse(dto, validatedProposal.getStatus());
        }

        // Blockchain server: request open pending [gRPC]
        OpenProposalPendingResponse pendedProposal = this.voteProposalProcessor.requestOpenPending(dto);

        if (!pendedProposal.getSuccess()) {
            return this.voteProposalProcessor.getErrorResponse(dto, pendedProposal.getStatus());
        }

        // Cache server: request cache proposal [gRPC]
        CacheProposalEventResponse cachedProposal = this.voteProposalProcessor.requestCacheProposal(dto);

        if (!cachedProposal.getCached()) {
            return this.voteProposalProcessor.getErrorResponse(dto, cachedProposal.getStatus());
        }

        // All steps passed
        return this.voteProposalProcessor.getSuccessResponse(dto);
    }
}
