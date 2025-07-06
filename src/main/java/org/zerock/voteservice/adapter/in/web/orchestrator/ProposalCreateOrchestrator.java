package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalCachingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.ProposalCreateWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalPendingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalValidationGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.ProposalCachingProcessor;
import org.zerock.voteservice.adapter.in.web.processor.ProposalPendingProcessor;
import org.zerock.voteservice.adapter.in.web.processor.ProposalValidationProcessor;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalPendingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalValidationResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class ProposalCreateOrchestrator extends AbstractOrchestrator<ProposalCreateWebClientRequestDto, ResponseDto> {

    private final ProposalValidationProcessor proposalValidationProcessor;
    private final ProposalPendingProcessor proposalPendingProcessor;
    private final ProposalCachingProcessor proposalCachingProcessor;

    public ProposalCreateOrchestrator(
            ControllerHelper controllerHelper,
            ProposalValidationProcessor proposalValidationProcessor,
            ProposalPendingProcessor proposalPendingProcessor,
            ProposalCachingProcessor proposalCachingProcessor
    ) {
        super(controllerHelper);
        this.proposalValidationProcessor = proposalValidationProcessor;
        this.proposalPendingProcessor = proposalPendingProcessor;
        this.proposalCachingProcessor = proposalCachingProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            ProposalCreateWebClientRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        if (userDetails == null) {
            return controllerHelper.failureHook(
                    logPrefix,
                    HttpStatus.UNAUTHORIZED,
                    "UNAUTHORIZED",
                    "인증 자격 증명이 부족합니다."
            );
        }

        log.debug("{}Attempting create propsoal for topic: {}", logPrefix, requestDto.getTopic());

        ProposalValidationGrpcRequestDto validationRequestDto = ProposalValidationGrpcRequestDto.builder()
                .topic(requestDto.getTopic())
                .build();

        GrpcProposalValidationResponseResult validationResult = processStep(
                proposalValidationProcessor, validationRequestDto, logPrefix, "Proposal Validation"
        );

        if (!validationResult.getSuccess()) {
            return createFailureResponse(
                    proposalValidationProcessor, validationResult, logPrefix, "Proposal Validation"
            );
        }

        ProposalPendingGrpcRequestDto pendingRequestDto = ProposalPendingGrpcRequestDto.builder()
                .topic(requestDto.getTopic())
                .duration(requestDto.getDuration())
                .build();

        GrpcProposalPendingResponseResult pendingResult = processStep(
                proposalPendingProcessor, pendingRequestDto, logPrefix, "Proposal Pending"
        );

        if (!pendingResult.getSuccess()) {
            return createFailureResponse(
                    proposalPendingProcessor, pendingResult, logPrefix, "Proposal Pending"
            );
        }

        ProposalCachingGrpcRequestDto cachingRequestDto = ProposalCachingGrpcRequestDto.builder()
                .topic(requestDto.getTopic())
                .duration(requestDto.getDuration())
                .options(requestDto.getOptions())
                .build();

        GrpcProposalCachingResponseResult cachingResult = processStep(
                proposalCachingProcessor, cachingRequestDto, logPrefix, "Proposal Caching"
        );

        if (!cachingResult.getSuccess()) {
            return createFailureResponse(
                    proposalCachingProcessor, cachingResult, logPrefix, "Ballot Caching"
            );
        }

        return createSuccessResponse(
                proposalCachingProcessor, cachingRequestDto, cachingResult, logPrefix, "Ballot Creation"
        );
    }
}
