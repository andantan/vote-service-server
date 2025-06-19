package org.zerock.voteservice.adapter.in.web.controller.vote;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.vote.docs.VoteProposalApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.vote.processor.ProposalCreateResult;
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
        ProposalCreateResult result = this.proposalCreateProcessor.processProposalCreation(dto);

        if (!result.getSuccess()) {
            return this.proposalCreateProcessor.getErrorResponse(result);
        }

        return this.proposalCreateProcessor.getSuccessResponse(dto, result);
    }
}
