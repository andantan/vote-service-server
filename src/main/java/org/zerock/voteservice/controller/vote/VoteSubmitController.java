package org.zerock.voteservice.controller.vote;

import domain.event.ballot.protocol.ValidateBallotEventResponse;
import domain.event.ballot.protocol.CacheBallotEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.controller.docs.VoteSubmitApiDoc;
import org.zerock.voteservice.dto.vote.VoteBallotRequestDto;
import org.zerock.voteservice.controller.vote.processor.VoteBallotProcessor;
import org.zerock.voteservice.dto.ResponseDto;


@RestController
public class VoteSubmitController extends VoteRequestMapper {
    private final VoteBallotProcessor voteBallotProcessor;

    public VoteSubmitController(VoteBallotProcessor voteBallotProcessor) {
        this.voteBallotProcessor = voteBallotProcessor;
    }

    @VoteSubmitApiDoc
    @PostMapping("/submit")
    public ResponseEntity<? extends ResponseDto> submitVote(@RequestBody VoteBallotRequestDto dto) {
        // Cache server: request validate ballot [gRPC]
        ValidateBallotEventResponse validatedBallot = this.voteBallotProcessor.validateBallot(dto);

        if (!validatedBallot.getValidation()) {
            return this.voteBallotProcessor.getErrorResponse(validatedBallot.getStatus());
        }

        // Blockchain server: request submit transaction [gRPC]
        SubmitBallotTransactionResponse submittedBallot = this.voteBallotProcessor.submitBallotTransaction(dto);

        if (!submittedBallot.getSuccess()) {
            return this.voteBallotProcessor.getErrorResponse(submittedBallot.getStatus());
        }

        // Cache server: request cache ballot [gRPC]
        CacheBallotEventResponse cachedBallot = this.voteBallotProcessor.cacheBallot(dto, submittedBallot.getVoteHash());

        if (!cachedBallot.getCached()) {
            return this.voteBallotProcessor.getErrorResponse(cachedBallot.getStatus());
        }

        return this.voteBallotProcessor.getSuccessResponse(dto, cachedBallot.getStatus(), submittedBallot.getVoteHash());
    }
}
