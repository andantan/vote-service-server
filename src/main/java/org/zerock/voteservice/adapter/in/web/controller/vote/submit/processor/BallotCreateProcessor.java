package org.zerock.voteservice.adapter.in.web.controller.vote.submit.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.adapter.common.ErrorResponseProcessor;
import org.zerock.voteservice.adapter.in.web.dto.vote.submit.VoteSubmitBallotDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.vote.BallotCreateProxy;

import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.submit.VoteSubmitRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.submit.VoteSubmitResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.status.VoteBallotErrorStatus;

import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.event.ballot.create.protocol.BallotCacheEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

@Log4j2
@Service
public class BallotCreateProcessor implements ErrorResponseProcessor {
    private final BallotCreateProxy ballotCreateProxy;

    public BallotCreateProcessor(BallotCreateProxy ballotCreateProxy) {
        this.ballotCreateProxy = ballotCreateProxy;
    }

    public BallotCreateProcessorResult processBallotCreation(VoteSubmitBallotDto dto) {
        // Cache server: request validate ballot [gRPC]
        BallotValidateEventResponse validatedBallot = this.ballotCreateProxy.validateBallot(dto);

        if (!validatedBallot.getValidation()) {
            return BallotCreateProcessorResult.failure(validatedBallot.getStatus());
        }

        // Blockchain server: request submit transaction [gRPC]
        SubmitBallotTransactionResponse submittedBallot = this.ballotCreateProxy.submitBallotTransaction(dto);

        if (!submittedBallot.getSuccess()) {
            return BallotCreateProcessorResult.failure(submittedBallot.getStatus());
        }

        // Cache server: request cache ballot [gRPC]
        BallotCacheEventResponse cachedBallot = this.ballotCreateProxy.cacheBallot(dto, submittedBallot.getVoteHash());

        if (!cachedBallot.getCached()) {
            return BallotCreateProcessorResult.failure(cachedBallot.getStatus());
        }

        return BallotCreateProcessorResult.success(cachedBallot.getStatus(), submittedBallot.getVoteHash());
    }

    public ResponseEntity<VoteSubmitResponseDto> getSuccessResponse(VoteSubmitBallotDto requestDto, BallotCreateProcessorResult result) {
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

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(BallotCreateProcessorResult result) {
        VoteBallotErrorStatus errorStatus = VoteBallotErrorStatus.fromCode(result.getStatus());
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(String status) {
        VoteBallotErrorStatus errorStatus = VoteBallotErrorStatus.fromCode(status);
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }
}
