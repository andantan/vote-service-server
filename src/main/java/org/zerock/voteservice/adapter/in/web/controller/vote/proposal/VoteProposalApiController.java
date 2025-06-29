package org.zerock.voteservice.adapter.in.web.controller.vote.proposal;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.vote.proposal.VoteProposalRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;
import org.zerock.voteservice.adapter.in.web.controller.vote.mapper.VoteApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.vote.proposal.docs.VoteProposalApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.vote.proposal.processor.ProposalCreateProcessor;
import org.zerock.voteservice.adapter.in.web.controller.vote.proposal.processor.ProposalCreateProcessorResult;

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
        String logPrefix;
        Integer currentUid;

        log.info(">>>>>> Initiating proposalVote API call: [Path: /proposal, Method: POST]");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserAuthenticationDetails userDetails) {
            currentUid = userDetails.getUid();
            logPrefix = String.format("[UID: %5d] ", currentUid);

            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(java.util.stream.Collectors.joining(", "));

            log.info("{}Authenticated User Info: [UID: {}, Roles: {}]", logPrefix, currentUid, roles);
        } else {
            log.warn("No valid user authentication found in SecurityContext, or not of UserAuthenticationDetails type.");
            return this.proposalCreateProcessor.getPanicResponse("UNKNOWN_ERROR");
        }

        log.info("{}Received Request DTO: [Topic: {}, Duration: {}, ...]", logPrefix, dto.getTopic(), dto.getDuration());

        ProposalCreateProcessorResult result = this.proposalCreateProcessor.processProposalCreation(dto);

        if (!result.getSuccess()) {
            log.warn("{}Vote proposal processing failed: [Status: {}, Message: {}]",
                    logPrefix, result.getStatus(), result.getMessage());
            return this.proposalCreateProcessor.getErrorResponse(result);
        }

        log.info("{}Vote proposal processing successful: [Status: {}, Message: {}]",
                logPrefix, result.getStatus(), result.getMessage());
        return this.proposalCreateProcessor.getSuccessResponse(dto, result);
    }
}
