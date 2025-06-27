package org.zerock.voteservice.adapter.in.web.controller.vote.proposal;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.vote.proposal.docs.VoteProposalApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.vote.proposal.processor.ProposalCreateProcessorResult;
import org.zerock.voteservice.adapter.in.web.controller.vote.mapper.VoteApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.proposal.VoteProposalRequestDto;
import org.zerock.voteservice.adapter.in.web.controller.vote.proposal.processor.ProposalCreateProcessor;

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
        ProposalCreateProcessorResult result = this.proposalCreateProcessor.processProposalCreation(dto);

        if (!result.getSuccess()) {
            return this.proposalCreateProcessor.getErrorResponse(result);
        }

        return this.proposalCreateProcessor.getSuccessResponse(dto, result);
    }
}
