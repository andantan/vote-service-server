package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalCachingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalPendingResponseResult;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalValidationResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalCachingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalCreateRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalPendingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalValidationRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.ProposalCachingProcessor;
import org.zerock.voteservice.adapter.in.web.processor.ProposalPendingProcesor;
import org.zerock.voteservice.adapter.in.web.processor.ProposalValidationProcessor;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@RestController
public class ProposalCreateApiController extends VoteApiEndpointMapper {

    private final ControllerHelper controllerHelper;
    private final ProposalValidationProcessor proposalValidationProcessor;
    private final ProposalPendingProcesor proposalPendingProcesor;
    private final ProposalCachingProcessor proposalCachingProcessor;

    public ProposalCreateApiController(
            ControllerHelper controllerHelper,
            ProposalValidationProcessor proposalValidationProcessor,
            ProposalPendingProcesor proposalPendingProcesor,
            ProposalCachingProcessor proposalCachingProcessor
    ) {
        this.controllerHelper = controllerHelper;
        this.proposalValidationProcessor = proposalValidationProcessor;
        this.proposalPendingProcesor = proposalPendingProcesor;
        this.proposalCachingProcessor = proposalCachingProcessor;
    }

    @PostMapping("/proposal")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> proposalVote(
            @RequestBody ProposalCreateRequestDto dto
    ) {

        UserAuthenticationDetails userDetails = this.controllerHelper.getUserDetails();

        Integer currentUid = userDetails.getUid();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating proposalVote API call: [Path: /proposal, Method: POST]", logPrefix);
        log.debug("{}Authenticated User Info: [Username: {}, Roles: {}]", logPrefix, userDetails.getUsername(), roles);
        log.debug("{}Received Request DTO: [Topic: {}, Duration: {}, ...]", logPrefix, dto.getTopic(), dto.getDuration());

        log.info("{}Attempting create proposal for topic: {}", logPrefix, dto.getTopic());

        // Start proposal validation
        log.debug("{}Starting Proposal Validation Process for topic: {}", logPrefix, dto.getTopic());

        ProposalValidationRequestDto validationRequestDto = ProposalValidationRequestDto.builder()
                .topic(dto.getTopic())
                .build();

        GrpcProposalValidationResponseResult validationResult = this.proposalValidationProcessor.execute(validationRequestDto);

        if (!validationResult.getSuccess()) {
            log.warn("{}Proposal Validation FAILED for topic: {}. Reason: {}", logPrefix, dto.getTopic(), validationResult.getMessage());

            return this.proposalValidationProcessor.getFailureResponseEntity(validationResult);
        }

        log.debug("{}Proposal Validation PASSED for topic: {}", logPrefix, dto.getTopic());

        // Start propsoal pending
        log.debug("{}Starting Proposal Pending Process to blockchain for topic: {}", logPrefix, dto.getTopic());

        ProposalPendingRequestDto pendingRequestDto = ProposalPendingRequestDto.builder()
                .topic(dto.getTopic())
                .duration(dto.getDuration())
                .build();

        GrpcProposalPendingResponseResult pendingResult = this.proposalPendingProcesor.execute(pendingRequestDto);

        if (!pendingResult.getSuccess()) {
            log.warn("{}Proposal Pending FAILED for topic: {}. Reason: {}", logPrefix, dto.getTopic(), pendingResult.getMessage());

            return this.proposalPendingProcesor.getFailureResponseEntity(pendingResult);
        }

        log.info("{}Proposal Pending PASSED for topic: {}", logPrefix, dto.getTopic());

        // Start proposal caching
        log.debug("{}Starting Proposal Caching Process for topic: {}", logPrefix, dto.getTopic());

        ProposalCachingRequestDto cachingRequestDto = ProposalCachingRequestDto.builder()
                .topic(dto.getTopic())
                .duration(dto.getDuration())
                .options(dto.getOptions())
                .build();

        GrpcProposalCachingResponseResult cachingResult = this.proposalCachingProcessor.execute(cachingRequestDto);

        if (cachingResult.getSuccess()) {
            log.info("{}Proposal Creation SUCCESS for topic: {}", logPrefix, dto.getTopic());
            return this.proposalCachingProcessor.getSuccessResponseEntity(cachingRequestDto, cachingResult);
        } else {
            log.warn("{}Proposal Caching FAILED for topic: {}. Reason: {}", logPrefix, dto.getTopic(), cachingResult.getMessage());
            return this.proposalCachingProcessor.getFailureResponseEntity(cachingResult);
        }
    }
}
