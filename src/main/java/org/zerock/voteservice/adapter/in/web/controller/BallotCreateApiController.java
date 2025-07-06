package org.zerock.voteservice.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "투표지 생성", description = "사용자 투표지 생성 및 검증 작업 API")
public class BallotCreateApiController extends VoteApiEndpointMapper {

    private final BallotCreateOrchestrator ballotCreateOrchestrator;

    public BallotCreateApiController(
            BallotCreateOrchestrator ballotCreateOrchestrator
    ) {
        this.ballotCreateOrchestrator = ballotCreateOrchestrator;
    }

    @Operation(summary = "새로운 투표지 제출", description = "투표지를 제출하고 블록체인에 기록")
    @PostMapping("/submit")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> submitVote(
            @RequestBody BallotCreateWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to BallotCreateOrchestrator.");

        return ballotCreateOrchestrator.orchestrate(dto);
    }
}
