package org.zerock.voteservice.controller.vote;

import domain.event.ballot.create.protocol.ValidateBallotEventResponse;
import domain.event.ballot.create.protocol.CacheBallotEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.controller.vote.docs.VoteSubmitApiDoc;
import org.zerock.voteservice.controller.mapper.ApiVoteEndpointMapper;
import org.zerock.voteservice.dto.vote.VoteBallotRequestDto;
import org.zerock.voteservice.controller.vote.processor.BallotProcessor;
import org.zerock.voteservice.dto.ResponseDto;


@RestController
public class ApiVoteSubmitController extends ApiVoteEndpointMapper {
    private final BallotProcessor processor;

    public ApiVoteSubmitController(BallotProcessor processor) {
        this.processor = processor;
    }

    @VoteSubmitApiDoc
    @PostMapping("/submit")
    public ResponseEntity<? extends ResponseDto> submitVote(@RequestBody VoteBallotRequestDto dto) {
        // Cache server: request validate ballot [gRPC]
        ValidateBallotEventResponse validatedBallot = this.processor.validateBallot(dto);

        if (!validatedBallot.getValidation()) {
            return this.processor.getErrorResponse(validatedBallot.getStatus());
        }

        // Blockchain server: request submit transaction [gRPC]
        SubmitBallotTransactionResponse submittedBallot = this.processor.submitBallotTransaction(dto);

        if (!submittedBallot.getSuccess()) {
            return this.processor.getErrorResponse(submittedBallot.getStatus());
        }

        // Cache server: request cache ballot [gRPC]
        CacheBallotEventResponse cachedBallot = this.processor.cacheBallot(dto, submittedBallot.getVoteHash());

        if (!cachedBallot.getCached()) {
            return this.processor.getErrorResponse(cachedBallot.getStatus());
        }

        return this.processor.getSuccessResponse(dto, cachedBallot.getStatus(), submittedBallot.getVoteHash());
    }
}
