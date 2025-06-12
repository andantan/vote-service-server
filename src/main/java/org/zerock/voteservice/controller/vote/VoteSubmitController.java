package org.zerock.voteservice.controller.vote;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import org.zerock.voteservice.controller.vote.processor.VoteBallotProcessor;
import org.zerock.voteservice.dto.vote.VoteBallotDto;

import domain.event.ballot.protocol.ValidateBallotEventResponse;
import domain.event.ballot.protocol.CacheBallotEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

import java.util.Map;

@Log4j2
@RestController
public class VoteSubmitController extends VoteRequestMapper {
    private final VoteBallotProcessor voteBallotProcessor;

    public VoteSubmitController(VoteBallotProcessor voteBallotProcessor) {
        this.voteBallotProcessor = voteBallotProcessor;
    }

    @PostMapping("/submit")
    public Map<String,String> submitVote(@RequestBody VoteBallotDto dto) {
        // Cache server: request validate ballot [gRPC]
        ValidateBallotEventResponse validatedBallot = this.voteBallotProcessor.validateBallot(dto);

        if (!validatedBallot.getValidation()) {
            return this.voteBallotProcessor.getErrorResponse(dto, validatedBallot.getStatus());
        }

        // Blockchain server: request submit transaction [gRPC]
        SubmitBallotTransactionResponse submittedBallot = this.voteBallotProcessor.submitBallotTransaction(dto);

        if (!submittedBallot.getSuccess()) {
            return this.voteBallotProcessor.getErrorResponse(dto, submittedBallot.getStatus());
        }

        // Cache server: request cache ballot [gRPC]
        CacheBallotEventResponse cachedBallot = this.voteBallotProcessor.cacheBallot(dto, submittedBallot.getVoteHash());

        if (!cachedBallot.getCached()) {
            return this.voteBallotProcessor.getErrorResponse(dto, cachedBallot.getStatus());
        }

        return this.voteBallotProcessor.getSuccessResponse(dto, submittedBallot.getVoteHash());
    }
}
