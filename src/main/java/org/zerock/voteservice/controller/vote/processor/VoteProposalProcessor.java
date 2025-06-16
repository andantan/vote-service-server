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
import org.zerock.voteservice.dto.vote.VoteErrorResponseDto;
import org.zerock.voteservice.dto.vote.error.VoteProposalErrorStatus;
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
        return this.grpcProposalEventClient.cacheProposal(dto.getTopic(), dto.getDuration(), dto.getOptions());
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

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(String internalStatus) {
        VoteProposalErrorStatus errorStatus = VoteProposalErrorStatus.fromCode(internalStatus);
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, errorStatus.getHttpStatusCode());
    }
}
