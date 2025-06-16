package org.zerock.voteservice.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.proposal.protocol.ValidateProposalEventResponse;
import domain.event.proposal.protocol.CacheProposalEventResponse;
import domain.vote.proposal.protocol.OpenProposalPendingResponse;

import org.zerock.voteservice.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.dto.vote.VoteProposalResponseDto;
import org.zerock.voteservice.grpc.event.GrpcProposalEventClient;
import org.zerock.voteservice.grpc.vote.GrpcProposalPendingClient;

import java.util.Objects;

@Log4j2
@Service
public class VoteProposalProcessor {
    private final GrpcProposalPendingClient grpcProposalPendingClient;
    private final GrpcProposalEventClient grpcProposalEventClient;

    public VoteProposalProcessor(
            GrpcProposalPendingClient grpcProposalPendingClient,
            GrpcProposalEventClient grpcProposalEventClient
    ) {
        this.grpcProposalPendingClient = grpcProposalPendingClient;
        this.grpcProposalEventClient = grpcProposalEventClient;
    }

    public ValidateProposalEventResponse validateProposal(VoteProposalRequestDto dto) {
        return this.grpcProposalEventClient.validateProposal(dto.getTopic());
    }

    public OpenProposalPendingResponse requestOpenPending(VoteProposalRequestDto dto) {
         return this.grpcProposalPendingClient.openProposalPending(dto.getTopic(), dto.getDuration());
    }

    public CacheProposalEventResponse requestCacheProposal(VoteProposalRequestDto dto) {
        return this.grpcProposalEventClient.cacheProposal(dto.getTopic(), dto.getDuration());
    }

public ResponseEntity<VoteProposalResponseDto> getSuccessResponse(VoteProposalRequestDto requestDto, String internalStatus) {
        VoteProposalResponseDto successDto = VoteProposalResponseDto.builder()
                .success(true)
                .topic(requestDto.getTopic())
                .message("투표 등록이 완료되었습니다.")
                .status(internalStatus)
                .httpStatusCode(HttpStatus.OK.value())
                .duration(requestDto.getDuration())
                .build();

        return new ResponseEntity<>(successDto, Objects.requireNonNull(HttpStatus.resolve(successDto.getHttpStatusCode())));
    }

    public ResponseEntity<VoteProposalResponseDto> getErrorResponse(VoteProposalRequestDto requestDto, String internalStatus) {
        String message;
        HttpStatus httpStatus;

        switch (internalStatus) {
            case "PROPOSAL_EXPIRED" -> {
                message = "이미 진행되었던 투표입니다.";
                httpStatus = HttpStatus.BAD_REQUEST; // 400
            }
            case "PROPOSAL_ALREADY_OPEN" -> {
                message = "현재 진행 중인 투표입니다.";
                httpStatus = HttpStatus.CONFLICT; // 409
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

        VoteProposalResponseDto errorDto = VoteProposalResponseDto.builder()
                .success(false)
                .topic(requestDto.getTopic())
                .message(message)
                .status(internalStatus)
                .httpStatusCode(httpStatus.value())
                .duration(requestDto.getDuration())
                .build();

        return new ResponseEntity<>(errorDto, Objects.requireNonNull(HttpStatus.resolve(errorDto.getHttpStatusCode())));
    }
}
