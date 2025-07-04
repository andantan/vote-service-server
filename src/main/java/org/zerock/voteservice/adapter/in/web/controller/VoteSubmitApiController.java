package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotTransactionResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotValidationResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotCachingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotCreateRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotTransactionRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotValidationRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.BallotCachingProcessor;
import org.zerock.voteservice.adapter.in.web.processor.BallotTransactionProcessor;
import org.zerock.voteservice.adapter.in.web.processor.BallotValidationProcessor;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.common.ResponseDto;

@Log4j2
@RestController
public class VoteSubmitApiController extends VoteApiEndpointMapper {

    private final ControllerHelper controllerHelper;
    private final BallotValidationProcessor ballotValidationProcessor;
    private final BallotTransactionProcessor ballotTransactionProcessor;
    private final BallotCachingProcessor ballotCachingProcessor;

    public VoteSubmitApiController(
            ControllerHelper controllerHelper,
            BallotValidationProcessor ballotValidationProcessor,
            BallotTransactionProcessor ballotTransactionProcessor,
            BallotCachingProcessor ballotCachingProcessor
    ) {
        this.controllerHelper = controllerHelper;
        this.ballotValidationProcessor = ballotValidationProcessor;
        this.ballotTransactionProcessor = ballotTransactionProcessor;
        this.ballotCachingProcessor = ballotCachingProcessor;
    }

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> submitVote(
            @RequestBody BallotCreateRequestDto dto
    ) {
        UserAuthenticationDetails userDetails = this.controllerHelper.getUserDetails();

        Integer currentUid = userDetails.getUid();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating submitVote API call: [Path: /submit, Method: POST]", logPrefix);
        log.debug("{}Authenticated User Info: [Username: {}, Roles: {}]", logPrefix, userDetails.getUsername(), roles);
        log.debug("{}Received Request DTO: [Topic: {}, Option: {}]", logPrefix, dto.getTopic(), dto.getOption());

        log.info("{}Attempting create ballot for topic: {}", logPrefix, dto.getTopic());

        // Start ballot validation
        log.debug("{}Starting Ballot Validation Process for topic: {}", logPrefix, dto.getTopic());

        BallotValidationRequestDto validationRequestDto = BallotValidationRequestDto.builder()
                .userHash(userDetails.getUserHash())
                .topic(dto.getTopic())
                .option(dto.getOption())
                .build();

        GrpcBallotValidationResponseResult validationResult = this.ballotValidationProcessor.execute(validationRequestDto);

        if (!validationResult.getSuccess()) {
            log.warn("{}Ballot Validation FAILED for topic: {}. Reason: {}", logPrefix, dto.getTopic(), validationResult.getMessage());

            return this.ballotValidationProcessor.getFailureResponseEntity(validationResult);
        }

        // Start ballot transaction
        log.debug("{}Starting Ballot Transaction Process to blockchain for topic: {}", logPrefix, dto.getTopic());

        BallotTransactionRequestDto transactionRequestDto = BallotTransactionRequestDto.builder()
                .userHash(userDetails.getUserHash())
                .topic(dto.getTopic())
                .option(dto.getOption())
                .build();

        GrpcBallotTransactionResponseResult transactionResult = this.ballotTransactionProcessor.execute(transactionRequestDto);

        if (!transactionResult.getSuccess()) {
            log.warn("{}Ballot Transaction FAILED for topic: {}", logPrefix, dto.getTopic());

            return this.ballotTransactionProcessor.getFailureResponseEntity(transactionResult);
        }

        log.info("{}Ballot Transaction PASSED for topic: {}", logPrefix, dto.getTopic());

        // Start ballot caching
        log.debug("{}Starting Ballot Caching Process for userHash: {}", logPrefix, userDetails.getUserHash());

        BallotCachingRequestDto cachingRequestDto = BallotCachingRequestDto.builder()
                .userHash(userDetails.getUserHash())
                .voteHash(transactionResult.getVoteHash())
                .option(dto.getOption())
                .build();

        GrpcBallotCachingResponseResult cachingResult = this.ballotCachingProcessor.execute(cachingRequestDto);

        if (cachingResult.getSuccess()) {
            log.info("{}Ballot Creation SUCCESS for topic: {}", logPrefix, dto.getTopic());
            return this.ballotCachingProcessor.getSuccessResponseEntity(cachingRequestDto, cachingResult);
        } else {
            log.warn("{}Ballot Caching FAILED for topic: {}. Reason: {}", logPrefix, dto.getTopic(), cachingResult.getMessage());
            return this.ballotCachingProcessor.getFailureResponseEntity(cachingResult);
        }
    }
}
