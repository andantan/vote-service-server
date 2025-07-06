package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotCachingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.BallotCreateWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotTransactionGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotValidationGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.BallotCachingProcessor;
import org.zerock.voteservice.adapter.in.web.processor.BallotTransactionProcessor;
import org.zerock.voteservice.adapter.in.web.processor.BallotValidationProcessor;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotTransactionResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotValidationResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class BallotCreateOrchestrator extends AbstractOrchestrator<BallotCreateWebClientRequestDto, ResponseDto> {

    private final BallotValidationProcessor ballotValidationProcessor;
    private final BallotTransactionProcessor ballotTransactionProcessor;
    private final BallotCachingProcessor ballotCachingProcessor;

    public BallotCreateOrchestrator(
            ControllerHelper controllerHelper,
            BallotValidationProcessor ballotValidationProcessor,
            BallotTransactionProcessor ballotTransactionProcessor,
            BallotCachingProcessor ballotCachingProcessor
    ) {
        super(controllerHelper);
        this.ballotValidationProcessor = ballotValidationProcessor;
        this.ballotTransactionProcessor = ballotTransactionProcessor;
        this.ballotCachingProcessor = ballotCachingProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            BallotCreateWebClientRequestDto requestDto,
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

        log.debug("{}Attempting create ballot for topic: {}", logPrefix, requestDto.getTopic());

        // 1. Ballot Validation Process
        BallotValidationGrpcRequestDto validationRequestDto = BallotValidationGrpcRequestDto.builder()
                .userHash(userDetails.getUserHash())
                .topic(requestDto.getTopic())
                .option(requestDto.getOption())
                .build();

        GrpcBallotValidationResponseResult validationResult = processStep(
                ballotValidationProcessor, validationRequestDto, logPrefix, "Ballot Validation"
        );

        if (!validationResult.getSuccess()) {
            return createFailureResponse(
                    ballotValidationProcessor, validationResult, logPrefix, "Ballot Validation"
            );
        }

        BallotTransactionGrpcRequestDto transactionRequestDto = BallotTransactionGrpcRequestDto.builder()
                .userHash(userDetails.getUserHash())
                .topic(requestDto.getTopic())
                .option(requestDto.getOption())
                .build();

        GrpcBallotTransactionResponseResult transactionResult = processStep(
                ballotTransactionProcessor, transactionRequestDto, logPrefix, "Ballot Transaction"
        );

        if (!transactionResult.getSuccess()) {
            return createFailureResponse(
                    ballotTransactionProcessor, transactionResult, logPrefix, "Ballot Transaction"
            );
        }

        // 3. Ballot Caching Process
        BallotCachingGrpcRequestDto cachingRequestDto = BallotCachingGrpcRequestDto.builder()
                .userHash(userDetails.getUserHash())
                .voteHash(transactionResult.getVoteHash())
                .topic(requestDto.getTopic())
                .build();

        GrpcBallotCachingResponseResult cachingResult = processStep(
                ballotCachingProcessor, cachingRequestDto, logPrefix, "Ballot Caching"
        );

        if (!cachingResult.getSuccess()) {
            return createFailureResponse(
                    ballotCachingProcessor, cachingResult, logPrefix, "Ballot Caching"
            );
        }

        return createSuccessResponse(
                ballotCachingProcessor, cachingRequestDto, cachingResult, logPrefix, "Ballot Creation"
        );
    }
}
