package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.orchestrator.BallotListQueryOrchestrator;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.BallotListQueryWebClientRequestDto;

import org.zerock.voteservice.adapter.in.common.ResponseDto;

@Log4j2
@RestController
public class BallotQueryApiController extends QueryApiEndpointMapper {

    private final BallotListQueryOrchestrator ballotListQueryOrchestrator;

    public BallotQueryApiController(BallotListQueryOrchestrator ballotListQueryOrchestrator) {
        this.ballotListQueryOrchestrator = ballotListQueryOrchestrator;
    }

    @GetMapping("/{userHash}/votes")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getUserVotes(
            @PathVariable(value = "userHash") final String userHash
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to BallotListQueryOrchestrator.");

        BallotListQueryWebClientRequestDto requestDto = BallotListQueryWebClientRequestDto.builder().userHash(userHash).build();

        return ballotListQueryOrchestrator.orchestrate(requestDto);
    }
}
