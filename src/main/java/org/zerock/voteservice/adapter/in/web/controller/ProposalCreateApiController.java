package org.zerock.voteservice.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "투표 생성", description = "투표 생성 및 검증 작업 API")
public class ProposalCreateApiController extends VoteApiEndpointMapper {

    private final ProposalCreateOrchestrator proposalCreateOrchestrator;

    public ProposalCreateApiController(ProposalCreateOrchestrator proposalCreateOrchestrator) {
        this.proposalCreateOrchestrator = proposalCreateOrchestrator;
    }

    @Operation(summary = "새로운 투표 생성", description = "투표를 생성하고 블록체인에 기록")
    @PostMapping("/proposal")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> proposalVote(
            @RequestBody ProposalCreateWebClientRequestDto dto
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to BallotCreateOrchestrator.");

        return proposalCreateOrchestrator.orchestrate(dto);
    }
}
