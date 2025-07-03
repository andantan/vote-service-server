package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import domain.vote.proposal.protocol.OpenProposalPendingResponse;
import domain.event.proposal.create.protocol.ProposalValidateEventResponse;
import domain.event.proposal.create.protocol.ProposalCacheEventResponse;

import org.zerock.voteservice.adapter.in.common.ErrorResponseProcessor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalCreateProxy;
import org.zerock.voteservice.adapter.in.web.dto.VoteProposalRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.VoteProposalResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.VoteErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.error.status.VoteProposalErrorStatus;


@Log4j2
@Service
public class ProposalCreateProcessor implements ErrorResponseProcessor {
    private final ProposalCreateProxy proposalCreateProxy;

    public ProposalCreateProcessor(ProposalCreateProxy proposalCreateProxy) {
        this.proposalCreateProxy = proposalCreateProxy;
    }

    public ProposalCreateProcessorResult processProposalCreation(VoteProposalRequestDto dto) {
        // Cache server: request validate proposal [gRPC]
        ProposalValidateEventResponse validatedProposal = this.proposalCreateProxy.validateProposal(dto);

        if (!validatedProposal.getValidation()) {
            return ProposalCreateProcessorResult.failure(validatedProposal.getStatus());
        }

        // Blockchain server: request open pending [gRPC]
        OpenProposalPendingResponse pendedProposal = this.proposalCreateProxy.requestOpenPending(dto);

        if (!pendedProposal.getSuccess()) {
            return ProposalCreateProcessorResult.failure(pendedProposal.getStatus());
        }

        // Cache server: request cache proposal [gRPC]
        ProposalCacheEventResponse cachedProposal = this.proposalCreateProxy.requestCacheProposal(dto);

        if (!cachedProposal.getCached()) {
            return ProposalCreateProcessorResult.failure(cachedProposal.getStatus());
        }

        return ProposalCreateProcessorResult.success(cachedProposal.getStatus(), dto.getTopic());

    }

    public ResponseEntity<VoteProposalResponseDto> getSuccessResponse(VoteProposalRequestDto requestDto, ProposalCreateProcessorResult result) {
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

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(ProposalCreateProcessorResult result) {
        VoteProposalErrorStatus errorStatus = VoteProposalErrorStatus.fromCode(result.getStatus());
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, errorStatus.getHttpStatusCode());
    }

    public ResponseEntity<VoteErrorResponseDto> getErrorResponse(String status) {
        VoteProposalErrorStatus errorStatus = VoteProposalErrorStatus.fromCode(status);
        VoteErrorResponseDto errorDto = VoteErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, errorStatus.getHttpStatusCode());
    }
}
