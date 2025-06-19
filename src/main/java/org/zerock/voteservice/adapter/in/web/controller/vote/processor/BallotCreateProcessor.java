package org.zerock.voteservice.adapter.in.web.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.adapter.in.web.dto.vote.VoteErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteSubmitRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteSubmitResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteBallotErrorStatus;
import org.zerock.voteservice.adapter.out.grpc.mongodbServer.voteData.BallotCreateEventServiceGrpcStub;
import org.zerock.voteservice.adapter.out.grpc.blockchainNode.BallotTransactionServiceGrpcStub;

import domain.event.ballot.create.protocol.BallotValidateEventResponse;
import domain.event.ballot.create.protocol.BallotCacheEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

@Log4j2
@Service
public class BallotCreateProcessor {

    private final BallotTransactionServiceGrpcStub ballotTransactionServiceGrpcStub;
    private final BallotCreateEventServiceGrpcStub ballotCreateEventServiceGrpcStub;


    public BallotCreateProcessor(
            BallotTransactionServiceGrpcStub ballotTransactionServiceGrpcStub,
            BallotCreateEventServiceGrpcStub ballotCreateEventServiceGrpcStub
    ) {
        this.ballotTransactionServiceGrpcStub = ballotTransactionServiceGrpcStub;
        this.ballotCreateEventServiceGrpcStub = ballotCreateEventServiceGrpcStub;
    }

    public BallotValidateEventResponse validateBallot(VoteSubmitRequestDto dto) {
        return this.ballotCreateEventServiceGrpcStub.validateBallot(
                dto.getUserHash(), dto.getTopic(), dto.getOption()
        );
    }

    public SubmitBallotTransactionResponse submitBallotTransaction(VoteSubmitRequestDto dto) {
        return this.ballotTransactionServiceGrpcStub.submitBallotTransaction(
                dto.getUserHash(), dto.getTopic(), dto.getOption()
        );
    }

    public BallotCacheEventResponse cacheBallot(VoteSubmitRequestDto dto, String voteHash) {
        return this.ballotCreateEventServiceGrpcStub.cacheBallot(
                dto.getUserHash(), voteHash, dto.getTopic()
        );
    }

    public ResponseEntity<VoteSubmitResponseDto> getSuccessResponse(VoteSubmitRequestDto requestDto, String internalStatus, String voteHash) {
        String successMessage = "투표 참여가 완료되었습니다.";

        VoteSubmitResponseDto successDto = VoteSubmitResponseDto.builder()
                .success(true)
                .topic(requestDto.getTopic())
                .message(successMessage)
                .status(internalStatus)
                .httpStatusCode(HttpStatus.OK.value())
                .userHash(requestDto.getUserHash())
                .voteHash(voteHash)
                .voteOption(requestDto.getOption())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(String internalStatus) {
        VoteBallotErrorStatus errorStatus = VoteBallotErrorStatus.fromCode(internalStatus);
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }
}
