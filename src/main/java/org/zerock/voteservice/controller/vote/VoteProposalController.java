package org.zerock.voteservice.controller.vote;

import domain.event.proposal.protocol.CacheProposalEventResponse;
import domain.vote.proposal.protocol.OpenProposalResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import domain.event.proposal.protocol.ValidateProposalEventResponse;

import org.zerock.voteservice.dto.vote.VoteProposalDto;
import org.zerock.voteservice.controller.vote.process.VoteProposalProcessor;

import java.util.Map;

@Log4j2
@RestController
public class VoteProposalController extends VoteRequestMapper {
    private final VoteProposalProcessor voteProposalProcessor;

    public VoteProposalController(VoteProposalProcessor voteProposalProcessor) {
        this.voteProposalProcessor = voteProposalProcessor;
    }

    @PostMapping("/proposal")
    public Map<String, String> proposalVote(@RequestBody VoteProposalDto dto) {
        // Cache server: request validate proposal
        ValidateProposalEventResponse validatedProposal = this.voteProposalProcessor.validateProposal(dto);

        if (!validatedProposal.getValidation()) {
            return this.voteProposalProcessor.getErrorResponse(dto, validatedProposal.getStatus());
        }

        // Blockchain server: request open pending
        OpenProposalResponse pendedProposal = this.voteProposalProcessor.requestOpenPending(dto);

        if (!pendedProposal.getSuccess()) {
            return this.voteProposalProcessor.getErrorResponse(dto, pendedProposal.getStatus());
        }

        // Cache server: request cache proposal
        CacheProposalEventResponse cachedProposal = this.voteProposalProcessor.requestCacheProposal(dto);

        if (!cachedProposal.getCached()) {
            return this.voteProposalProcessor.getErrorResponse(dto, cachedProposal.getStatus());
        }

        // All steps get fine
        return this.voteProposalProcessor.getSuccessResponse(dto);
    }
}
