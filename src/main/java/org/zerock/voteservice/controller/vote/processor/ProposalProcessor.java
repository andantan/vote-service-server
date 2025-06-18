package org.zerock.voteservice.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.proposal.create.protocol.ValidateProposalEventResponse;
import domain.event.proposal.create.protocol.CacheProposalEventResponse;
import domain.vote.proposal.protocol.OpenProposalPendingResponse;

import org.zerock.voteservice.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.dto.vote.VoteProposalResponseDto;
import org.zerock.voteservice.dto.vote.VoteErrorResponseDto;
import org.zerock.voteservice.dto.vote.error.VoteProposalErrorStatus;
import org.zerock.voteservice.grpc.event.GrpcProposalCreateEventClient;
import org.zerock.voteservice.grpc.vote.GrpcProposalPendingClient;


@Log4j2
@Service
public class ProposalProcessor {
    private final GrpcProposalPendingClient grpcPendingClient;
    private final GrpcProposalCreateEventClient grpcEventClient;

    public ProposalProcessor(
            GrpcProposalPendingClient grpcPendingClient,
            GrpcProposalCreateEventClient grpcEventClient
    ) {
        this.grpcPendingClient = grpcPendingClient;
        this.grpcEventClient = grpcEventClient;
    }

    public ValidateProposalEventResponse validateProposal(VoteProposalRequestDto dto) {
        return this.grpcEventClient.validateProposal(dto.getTopic());
    }

    public OpenProposalPendingResponse requestOpenPending(VoteProposalRequestDto dto) {
         return this.grpcPendingClient.openProposalPending(dto.getTopic(), dto.getDuration());
    }

    public CacheProposalEventResponse requestCacheProposal(VoteProposalRequestDto dto) {
        return this.grpcEventClient.cacheProposal(dto.getTopic(), dto.getDuration(), dto.getOptions());
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

    return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(String internalStatus) {
        VoteProposalErrorStatus errorStatus = VoteProposalErrorStatus.fromCode(internalStatus);
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, errorStatus.getHttpStatusCode());
    }
}
