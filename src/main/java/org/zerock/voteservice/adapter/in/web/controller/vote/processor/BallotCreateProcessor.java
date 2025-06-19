package org.zerock.voteservice.adapter.in.web.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.adapter.out.grpc.proxy.vote.BallotCreateProxy;

import org.zerock.voteservice.adapter.in.web.dto.vote.VoteErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteSubmitRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteSubmitResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteBallotErrorStatus;

import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.event.ballot.create.protocol.BallotCacheEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

@Log4j2
@Service
public class BallotCreateProcessor {
    private final BallotCreateProxy ballotCreateProxy;

    public BallotCreateProcessor(BallotCreateProxy ballotCreateProxy) {
        this.ballotCreateProxy = ballotCreateProxy;
    }

    public BallotCreateResult processBallotCreation(VoteSubmitRequestDto dto) {
        // Cache server: request validate ballot [gRPC]
        BallotValidateEventResponse validatedBallot = this.ballotCreateProxy.validateBallot(dto);

        if (!validatedBallot.getValidation()) {
            return BallotCreateResult.failure(validatedBallot.getStatus());
        }

        // Blockchain server: request submit transaction [gRPC]
        SubmitBallotTransactionResponse submittedBallot = this.ballotCreateProxy.submitBallotTransaction(dto);

        if (!submittedBallot.getSuccess()) {
            return BallotCreateResult.failure(submittedBallot.getStatus());
        }

        // Cache server: request cache ballot [gRPC]
        BallotCacheEventResponse cachedBallot = this.ballotCreateProxy.cacheBallot(dto, submittedBallot.getVoteHash());

        if (!cachedBallot.getCached()) {
            return BallotCreateResult.failure(cachedBallot.getStatus());
        }

        return BallotCreateResult.success(cachedBallot.getStatus(), submittedBallot.getVoteHash());
    }

    public ResponseEntity<VoteSubmitResponseDto> getSuccessResponse(VoteSubmitRequestDto requestDto, BallotCreateResult result) {
        String successMessage = "투표 참여가 완료되었습니다.";

        VoteSubmitResponseDto successDto = VoteSubmitResponseDto.builder()
                .success(result.getSuccess())
                .topic(requestDto.getTopic())
                .message(successMessage)
                .status(result.getStatus())
                .httpStatusCode(HttpStatus.OK.value())
                .userHash(requestDto.getUserHash())
                .voteHash(result.getVoteHash())
                .voteOption(requestDto.getOption())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(BallotCreateResult result) {
        VoteBallotErrorStatus errorStatus = VoteBallotErrorStatus.fromCode(result.getStatus());
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }
}
