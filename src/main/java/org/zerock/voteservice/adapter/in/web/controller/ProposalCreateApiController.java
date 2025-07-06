package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.mapper.VoteApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.orchestrator.ProposalCreateOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.ProposalCreateWebClientRequestDto;

@Log4j2
@RestController
public class ProposalCreateApiController extends VoteApiEndpointMapper {

    private final ProposalCreateOrchestrator proposalCreateOrchestrator;

    public ProposalCreateApiController(ProposalCreateOrchestrator proposalCreateOrchestrator) {
        this.proposalCreateOrchestrator = proposalCreateOrchestrator;
    }

    @PostMapping("/proposal")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> proposalVote(
            @RequestBody ProposalCreateWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to BallotCreateOrchestrator.");

        return proposalCreateOrchestrator.orchestrate(dto);
    }
}
