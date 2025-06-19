package org.zerock.voteservice.adapter.in.web.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import org.zerock.voteservice.adapter.in.web.dto.vote.VoteProposalRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteProposalResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.VoteErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteProposalErrorStatus;

import org.zerock.voteservice.adapter.out.grpc.mongodbServer.voteData.ProposalCreateEventServiceGrpcStub;
import org.zerock.voteservice.adapter.out.grpc.blockchainNode.ProposalPendingServiceGrpcStub;


@Log4j2
@Service
public class ProposalCreateProcessor {
    private final ProposalCreateEventServiceGrpcStub proposalCreateEventServiceGrpcStub;
    private final ProposalPendingServiceGrpcStub proposalPendingServiceGrpcStub;

    public ProposalCreateProcessor(
            ProposalPendingServiceGrpcStub proposalPendingServiceGrpcStub,
            ProposalCreateEventServiceGrpcStub proposalCreateEventServiceGrpcStub
    ) {
        this.proposalCreateEventServiceGrpcStub = proposalCreateEventServiceGrpcStub;
        this.proposalPendingServiceGrpcStub = proposalPendingServiceGrpcStub;
    }

    public ProposalValidateEventResponse validateProposal(VoteProposalRequestDto dto) {
        return this.proposalCreateEventServiceGrpcStub.validateProposal(dto.getTopic());
    }

    public OpenProposalPendingResponse requestOpenPending(VoteProposalRequestDto dto) {
         return this.proposalPendingServiceGrpcStub.openProposalPending(dto.getTopic(), dto.getDuration());
    }

    public ProposalCacheEventResponse requestCacheProposal(VoteProposalRequestDto dto) {
        return this.proposalCreateEventServiceGrpcStub.cacheProposal(dto.getTopic(), dto.getDuration(), dto.getOptions());
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
