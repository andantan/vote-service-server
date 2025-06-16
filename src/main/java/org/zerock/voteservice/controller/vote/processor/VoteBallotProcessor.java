package org.zerock.voteservice.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.dto.vote.VoteErrorResponseDto;
import org.zerock.voteservice.dto.vote.VoteBallotRequestDto;
import org.zerock.voteservice.dto.vote.VoteBallotResponseDto;
import org.zerock.voteservice.dto.vote.error.VoteBallotErrorStatus;
import org.zerock.voteservice.grpc.event.GrpcBallotEventClient;
import org.zerock.voteservice.grpc.vote.GrpcBallotTransactionClient;

import domain.event.ballot.protocol.ValidateBallotEventResponse;
import domain.event.ballot.protocol.CacheBallotEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

import java.util.Objects;

@Log4j2
@Service
public class VoteBallotProcessor {

    private final GrpcBallotTransactionClient grpcBallotTransactionClient;
    private final GrpcBallotEventClient grpcBallotEventClient;


    public VoteBallotProcessor(
            GrpcBallotTransactionClient grpcBallotTransactionClient,
            GrpcBallotEventClient grpcBallotEventClient
    ) {
        this.grpcBallotTransactionClient = grpcBallotTransactionClient;
        this.grpcBallotEventClient = grpcBallotEventClient;
    }

    public ValidateBallotEventResponse validateBallot(VoteBallotRequestDto dto) {
        return this.grpcBallotEventClient.validateBallot(dto.getUserHash(), dto.getTopic(), dto.getOption());
    }

    public SubmitBallotTransactionResponse submitBallotTransaction(VoteBallotRequestDto dto) {
        return this.grpcBallotTransactionClient.submitBallotTransaction(
                dto.getUserHash(), dto.getTopic(), dto.getOption()
        );
    }

    public CacheBallotEventResponse cacheBallot(VoteBallotRequestDto dto, String voteHash) {
        return this.grpcBallotEventClient.cacheBallot(
                dto.getUserHash(), voteHash, dto.getTopic(), dto.getOption()
        );
    }

    public ResponseEntity<VoteBallotResponseDto> getSuccessResponse(VoteBallotRequestDto requestDto, String internalStatus, String voteHash) {
        String successMessage = "투표 참여가 완료되었습니다.";

        VoteBallotResponseDto successDto = VoteBallotResponseDto.builder()
                .success(true)
                .topic(requestDto.getTopic())
                .message(successMessage)
                .status(internalStatus)
                .httpStatusCode(HttpStatus.OK.value())
                .userHash(requestDto.getUserHash())
                .voteHash(voteHash)
                .voteOption(requestDto.getOption())
                .build();

        return new ResponseEntity<>(successDto, Objects.requireNonNull(HttpStatus.resolve(successDto.getHttpStatusCode())));
    }

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(String internalStatus) {
        VoteBallotErrorStatus errorStatus = VoteBallotErrorStatus.fromCode(internalStatus);
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }
}
