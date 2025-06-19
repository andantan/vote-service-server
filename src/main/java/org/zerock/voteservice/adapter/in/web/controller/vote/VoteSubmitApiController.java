package org.zerock.voteservice.adapter.in.web.controller.vote;

import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.event.ballot.create.protocol.BallotCacheEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.adapter.in.web.controller.vote.docs.VoteSubmitApiDoc;
import org.zerock.voteservice.adapter.in.web.mapper.VoteApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteSubmitRequestDto;
import org.zerock.voteservice.adapter.in.web.controller.vote.processor.BallotCreateProcessor;
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
        // Cache server: request validate ballot [gRPC]
        BallotValidateEventResponse validatedBallot = this.ballotCreateProcessor.validateBallot(dto);

        if (!validatedBallot.getValidation()) {
            return this.ballotCreateProcessor.getErrorResponse(validatedBallot.getStatus());
        }

        // Blockchain server: request submit transaction [gRPC]
        SubmitBallotTransactionResponse submittedBallot = this.ballotCreateProcessor.submitBallotTransaction(dto);

        if (!submittedBallot.getSuccess()) {
            return this.ballotCreateProcessor.getErrorResponse(submittedBallot.getStatus());
        }

        // Cache server: request cache ballot [gRPC]
        BallotCacheEventResponse cachedBallot = this.ballotCreateProcessor.cacheBallot(dto, submittedBallot.getVoteHash());

        if (!cachedBallot.getCached()) {
            return this.ballotCreateProcessor.getErrorResponse(cachedBallot.getStatus());
        }

        return this.ballotCreateProcessor.getSuccessResponse(dto, cachedBallot.getStatus(), submittedBallot.getVoteHash());
    }
}
