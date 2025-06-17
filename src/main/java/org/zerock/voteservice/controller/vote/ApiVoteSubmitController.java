package org.zerock.voteservice.controller.vote;

import domain.event.ballot.create.protocol.ValidateBallotEventResponse;
import domain.event.ballot.create.protocol.CacheBallotEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.controller.docs.VoteSubmitApiDoc;
import org.zerock.voteservice.controller.mapper.ApiVoteEndpointMapper;
import org.zerock.voteservice.dto.vote.VoteBallotRequestDto;
import org.zerock.voteservice.controller.vote.processor.BallotProcessor;
import org.zerock.voteservice.dto.ResponseDto;


@RestController
public class ApiVoteSubmitController extends ApiVoteEndpointMapper {
    private final BallotProcessor ballotProcessor;

    public ApiVoteSubmitController(BallotProcessor ballotProcessor) {
        this.ballotProcessor = ballotProcessor;
    }

    @VoteSubmitApiDoc
    @PostMapping("/submit")
    public ResponseEntity<? extends ResponseDto> submitVote(@RequestBody VoteBallotRequestDto dto) {
        // Cache server: request validate ballot [gRPC]
        ValidateBallotEventResponse validatedBallot = this.ballotProcessor.validateBallot(dto);

        if (!validatedBallot.getValidation()) {
            return this.ballotProcessor.getErrorResponse(validatedBallot.getStatus());
        }

        // Blockchain server: request submit transaction [gRPC]
        SubmitBallotTransactionResponse submittedBallot = this.ballotProcessor.submitBallotTransaction(dto);

        if (!submittedBallot.getSuccess()) {
            return this.ballotProcessor.getErrorResponse(submittedBallot.getStatus());
        }

        // Cache server: request cache ballot [gRPC]
        CacheBallotEventResponse cachedBallot = this.ballotProcessor.cacheBallot(dto, submittedBallot.getVoteHash());

        if (!cachedBallot.getCached()) {
            return this.ballotProcessor.getErrorResponse(cachedBallot.getStatus());
        }

        return this.ballotProcessor.getSuccessResponse(dto, cachedBallot.getStatus(), submittedBallot.getVoteHash());
    }
}
