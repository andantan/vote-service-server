package org.zerock.voteservice.adapter.in.web.controller.vote.proposal.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import org.zerock.voteservice.adapter.out.grpc.proxy.vote.ProposalCreateProxy;
import org.zerock.voteservice.adapter.in.web.dto.vote.proposal.VoteProposalRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.proposal.VoteProposalResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.status.VoteProposalErrorStatus;


@Log4j2
@Service
public class ProposalCreateProcessor {
    private final ProposalCreateProxy proposalCreateProxy;

    public ProposalCreateProcessor(ProposalCreateProxy proposalCreateProxy) {
        this.proposalCreateProxy = proposalCreateProxy;
    }

    public ProposalCreateResult processProposalCreation(VoteProposalRequestDto dto) {
        // Cache server: request validate proposal [gRPC]
        ProposalValidateEventResponse validatedProposal = this.proposalCreateProxy.validateProposal(dto);

        if (!validatedProposal.getValidation()) {
            return ProposalCreateResult.failure(validatedProposal.getStatus());
        }

        // Blockchain server: request open pending [gRPC]
        OpenProposalPendingResponse pendedProposal = this.proposalCreateProxy.requestOpenPending(dto);

        if (!pendedProposal.getSuccess()) {
            return ProposalCreateResult.failure(pendedProposal.getStatus());
        }

        // Cache server: request cache proposal [gRPC]
        ProposalCacheEventResponse cachedProposal = this.proposalCreateProxy.requestCacheProposal(dto);

        if (!cachedProposal.getCached()) {
            return ProposalCreateResult.failure(cachedProposal.getStatus());
        }

        return ProposalCreateResult.success(cachedProposal.getStatus(), dto.getTopic());

    }

    public ResponseEntity<VoteProposalResponseDto> getSuccessResponse(VoteProposalRequestDto requestDto, ProposalCreateResult result) {
            VoteProposalResponseDto successDto = VoteProposalResponseDto.builder()
                    .success(result.getSuccess())
                    .topic(result.getTopic())
                    .message(result.getMessage())
                    .status(result.getStatus())
                    .httpStatusCode(result.getHttpStatusCode())
                    .duration(requestDto.getDuration())
                    .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(ProposalCreateResult result) {
        VoteProposalErrorStatus errorStatus = VoteProposalErrorStatus.fromCode(result.getStatus());
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, errorStatus.getHttpStatusCode());
    }
}
