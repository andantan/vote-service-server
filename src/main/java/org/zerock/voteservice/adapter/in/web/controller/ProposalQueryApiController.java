package org.zerock.voteservice.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import org.zerock.voteservice.adapter.in.web.controller.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.ProposalDetailQueryWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.orchestrator.ProposalDetailQueryOrchestrator;
import org.zerock.voteservice.adapter.in.web.orchestrator.ProposalFilteredListQueryOrchestrator;

import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.ProposalFilteredListQueryWebClientRequestDto;

@Log4j2
@RestController
@Tag(name = "투표 내역 조회", description = "투표 내역 조회 작업 API")
public class ProposalQueryApiController extends QueryApiEndpointMapper {

    private final ProposalDetailQueryOrchestrator proposalDetailQueryOrchestrator;
    private final ProposalFilteredListQueryOrchestrator proposalFilteredListQueryOrchestrator;

    public ProposalQueryApiController(
            ProposalDetailQueryOrchestrator proposalDetailQueryOrchestrator,
            ProposalFilteredListQueryOrchestrator proposalFilteredListQueryOrchestrator
    ) {
        this.proposalDetailQueryOrchestrator = proposalDetailQueryOrchestrator;
        this.proposalFilteredListQueryOrchestrator = proposalFilteredListQueryOrchestrator;
    }

    @Operation(summary = "투표 상세 내역 조회", description = "해당 Topic 값에 대한 투표 상세 내역 조회")
    @GetMapping("/proposal/{topic}/detail")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getProposalDetail(
            @PathVariable(value = "topic") final String topic
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to ProposalDetailQueryOrchestrator.");

        ProposalDetailQueryWebClientRequestDto requestDto = ProposalDetailQueryWebClientRequestDto.builder()
                .topic(topic)
                .build();

        return proposalDetailQueryOrchestrator.orchestrate(requestDto);
    }

    @Operation(summary = "투표 내역 리스트 조회", description = "해당 필터링 값에 대한 투표 내역 리스트 조회")
    @GetMapping("/proposal/list")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getFilteredProposals(
            @RequestParam(name = "summarize", defaultValue = "false") Boolean summarize,
            @RequestParam(name = "expired", required = false) Boolean expired,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder,
            @RequestParam(name = "sortBy", defaultValue = "expiredAt") String sortBy,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "15") Integer limit
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to ProposalFilteredListQueryOrchestrator.");

        Integer skip = (page - 1) * limit;

        ProposalFilteredListQueryWebClientRequestDto requestDto = ProposalFilteredListQueryWebClientRequestDto.builder()
                .summarize(summarize)
                .expired(expired)
                .sortOrder(sortOrder)
                .sortBy(sortBy)
                .skip(skip)
                .limit(limit)
                .build();

        return proposalFilteredListQueryOrchestrator.orchestrate(requestDto);
    }
}
