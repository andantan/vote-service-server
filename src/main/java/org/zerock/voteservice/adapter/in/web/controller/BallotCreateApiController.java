package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.mapper.VoteApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.orchestrator.BallotCreateOrchestrator;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.BallotCreateWebClientRequestDto;

@Log4j2
@RestController
public class BallotCreateApiController extends VoteApiEndpointMapper {

    private final BallotCreateOrchestrator ballotCreateOrchestrator;

    public BallotCreateApiController(
            BallotCreateOrchestrator ballotCreateOrchestrator
    ) {
        this.ballotCreateOrchestrator = ballotCreateOrchestrator;
    }

    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> submitVote(
            @RequestBody BallotCreateWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to BallotCreateOrchestrator.");

        return ballotCreateOrchestrator.orchestrate(dto);
    }
}
