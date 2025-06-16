package org.zerock.voteservice.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.dto.vote.VoteBallotRequestDto;
import org.zerock.voteservice.dto.vote.VoteBallotResponseDto;
import org.zerock.voteservice.grpc.event.GrpcBallotEventClient;
import org.zerock.voteservice.grpc.vote.GrpcBallotTransactionClient;
import org.zerock.voteservice.property.event.GrpcBallotEventConnectionProperties;
import org.zerock.voteservice.property.vote.GrpcBallotTransactionConnectionProperties;

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
            GrpcBallotTransactionConnectionProperties grpcBallotTransactionConnectionProperties,
            GrpcBallotEventConnectionProperties grpcBallotEventConnectionProperties
    ) {
        this.grpcBallotTransactionClient = new GrpcBallotTransactionClient(
                grpcBallotTransactionConnectionProperties.getHost(), grpcBallotTransactionConnectionProperties.getPort()
        );
        this.grpcBallotEventClient = new GrpcBallotEventClient(
                grpcBallotEventConnectionProperties.getHost(), grpcBallotEventConnectionProperties.getPort()
        );
    }

    public ValidateBallotEventResponse validateBallot(VoteBallotRequestDto dto) {
        return this.grpcBallotEventClient.validateBallot(dto.getUserHash(), dto.getTopic());
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

    public ResponseEntity<VoteBallotResponseDto> getErrorResponse(VoteBallotRequestDto requestDto, String internalStatus) {
        String errorVoteHash = "0000000000000000000000000000000000000000000000000000000000000000";
        String message;
        HttpStatus httpStatus;

        switch (internalStatus) {
            case "DUPLICATE_VOTE_SUBMISSION" -> {
                message = "이미 참가한 투표입니다. (재투표 불가)";
                httpStatus = HttpStatus.CONFLICT; // 409
            }
            case "PROPOSAL_NOT_OPEN" -> {
                message = "현재 존재하지 않는 투표입니다.";
                httpStatus = HttpStatus.NOT_FOUND; // 404
            }
            case "TIMEOUT_PROPOSAL" -> {
                message = "투표가 마감되어 정산 중입니다.";
                httpStatus = HttpStatus.NOT_ACCEPTABLE; // 406
            }
            case "INVALID_HASH_LENGTH" -> {
                message = "비정상적인 해시값입니다. (해시 길이 오류)";
                httpStatus = HttpStatus.BAD_REQUEST; // 400
            }
            case "DECODE_ERROR" -> {
                message = "비정상적인 해시값입니다. (해시 해독 오류)";
                httpStatus = HttpStatus.BAD_REQUEST; // 400
            }
            case "UNKNOWN_ERROR" -> {
                message = "알 수 없는 오류가 발생했습니다.";
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // 500
            }
            default -> {
                message = "요청 처리 중 오류가 발생했습니다.";
                httpStatus = HttpStatus.BAD_REQUEST; // 400
            }
        }

        VoteBallotResponseDto successDto = VoteBallotResponseDto.builder()
                .success(false)
                .topic(requestDto.getTopic())
                .message(message)
                .status(internalStatus)
                .httpStatusCode(httpStatus.value())
                .userHash(requestDto.getUserHash())
                .voteHash(errorVoteHash)
                .voteOption(requestDto.getOption())
                .build();

        return new ResponseEntity<>(successDto, java.util.Objects.requireNonNull(HttpStatus.resolve(successDto.getHttpStatusCode())));
    }
}
