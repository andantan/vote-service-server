package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.common.GrpcExceptionHandler;
import org.zerock.voteservice.adapter.in.web.dto.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.VoteProposalRequestDto;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.controller.docs.proposal.VoteProposalApiDoc;
import org.zerock.voteservice.adapter.in.web.processor.ProposalCreateProcessor;
import org.zerock.voteservice.adapter.in.web.processor.ProposalCreateProcessorResult;
import org.zerock.voteservice.adapter.out.grpc.stub.common.exception.GrpcServiceUnavailableException;

@Log4j2
@RestController
public class VoteProposalApiController extends VoteApiEndpointMapper {
    private final ProposalCreateProcessor proposalCreateProcessor;

    public VoteProposalApiController(ProposalCreateProcessor proposalCreateProcessor) {
        this.proposalCreateProcessor = proposalCreateProcessor;
    }

    @VoteProposalApiDoc
    @PostMapping("/proposal")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> proposalVote(@RequestBody VoteProposalRequestDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        Integer currentUid = userDetails.getUid();
        String logPrefix = String.format("[UID:%d] ", currentUid);

        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        log.debug("{}>>>>>> Initiating proposalVote API call: [Path: /proposal, Method: POST]", logPrefix);
        log.debug("{}Authenticated User Info: [Username: {}, Roles: {}]", logPrefix, userDetails.getUsername(), roles);
        log.debug("{}Received Request DTO: [Topic: {}, Duration: {}, ...]", logPrefix, dto.getTopic(), dto.getDuration());

        try {
            ProposalCreateProcessorResult result = this.proposalCreateProcessor.processProposalCreation(dto);

            if (!result.getSuccess()) {
                log.warn("{}Vote proposal processing failed: [Topic: {}, Status: {}, Message: {}]",
                        logPrefix, dto.getTopic(), result.getStatus(), result.getMessage());
                return this.proposalCreateProcessor.getErrorResponse(result);
            }

            log.info("{}Vote proposal processing successful: [Topic: {}, Status: {}, Message: {}]",
                    logPrefix, dto.getTopic(), result.getStatus(), result.getMessage());
            return this.proposalCreateProcessor.getSuccessResponse(dto, result);

        } catch (GrpcServiceUnavailableException e) {
            log.error("{}{}", logPrefix, e.getMessage());

            return this.proposalCreateProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");

        } catch (io.grpc.StatusRuntimeException e) {
            return GrpcExceptionHandler.handleGrpcStatusRuntimeExceptionInController(
                    currentUid, e, this.proposalCreateProcessor
            );

        } catch (Exception e) {
            log.error("{}An unexpected error occurred during vote proposal for topic: {}, duration: {}. Error: {}", logPrefix, dto.getTopic(), dto.getDuration(), e.getMessage(), e);

            return this.proposalCreateProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
