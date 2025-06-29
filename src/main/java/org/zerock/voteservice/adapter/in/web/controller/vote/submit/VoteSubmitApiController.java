package org.zerock.voteservice.adapter.in.web.controller.vote.submit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.adapter.in.web.controller.vote.submit.docs.VoteSubmitApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.vote.submit.processor.BallotCreateProcessorResult;
import org.zerock.voteservice.adapter.in.web.controller.vote.mapper.VoteApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.dto.vote.submit.VoteSubmitRequestDto;
import org.zerock.voteservice.adapter.in.web.controller.vote.submit.processor.BallotCreateProcessor;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;


@RestController
public class VoteSubmitApiController extends VoteApiEndpointMapper {
    private final BallotCreateProcessor ballotCreateProcessor;

    public VoteSubmitApiController(BallotCreateProcessor ballotCreateProcessor) {
        this.ballotCreateProcessor = ballotCreateProcessor;
    }

    @VoteSubmitApiDoc
    @PostMapping("/submit")
    public ResponseEntity<? extends ResponseDto> submitVote(@RequestBody VoteSubmitRequestDto dto) {
        BallotCreateProcessorResult result = this.ballotCreateProcessor.processBallotCreation(dto);

        if (!result.getSuccess()) {
            return this.ballotCreateProcessor.getErrorResponse(result);
        }

        return this.ballotCreateProcessor.getSuccessResponse(dto, result);
    }
}
