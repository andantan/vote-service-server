package org.zerock.voteservice.adapter.in.web.controller;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.access.prepost.PreAuthorize;

import org.zerock.voteservice.security.user.UserAuthenticationDetails;

import org.zerock.voteservice.adapter.in.web.processor.impl.ProposalDetailQueryProcessor;
import org.zerock.voteservice.adapter.in.web.processor.impl.ProposalFilteredListQueryProcessor;

import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalDetailQueryResult;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalFilteredListQueryResult;

@Log4j2
@RestController
public class QueryProposalApiController extends QueryApiEndpointMapper {

    private final ControllerHelper controllerHelper;
    private final ProposalDetailQueryProcessor proposalDetailQueryProcessor;
    private final ProposalFilteredListQueryProcessor proposalFilteredListQueryProcessor;

    public QueryProposalApiController(
            ControllerHelper controllerHelper,
            ProposalDetailQueryProcessor proposalDetailQueryProcessor,
            ProposalFilteredListQueryProcessor proposalFilteredListQueryProcessor
    ) {
        this.controllerHelper = controllerHelper;
        this.proposalDetailQueryProcessor = proposalDetailQueryProcessor;
        this.proposalFilteredListQueryProcessor = proposalFilteredListQueryProcessor;
    }

    @GetMapping("/proposal/{topic}/detail")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getProposalDetail(
            @PathVariable(value = "topic") final String topic
    ) {

        UserAuthenticationDetails userDetails = this.controllerHelper.getUserDetails();

        Integer currentUid = userDetails.getUid();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating getProposalDetail API call: [Path: /proposal/{}/detail, Method: GET]", logPrefix, topic);
        log.debug("{}Authenticated User Info: [Username: {}, Role: {}]", logPrefix, userDetails.getUsername(), role);
        log.debug("{}Received Path Variable: [Topic: {}]", logPrefix, topic);

        ProposalDetailQueryRequestDto requestDto = ProposalDetailQueryRequestDto
                .builder()
                .topic(topic)
                .build();

        GrpcProposalDetailQueryResult result = this.proposalDetailQueryProcessor.process(requestDto);

        return result.getSuccess()
                ? this.proposalDetailQueryProcessor.getSuccessResponseEntity(requestDto, result)
                : this.proposalDetailQueryProcessor.getFailureResponseEntity(result);
    }

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

        UserAuthenticationDetails userDetails = this.controllerHelper.getUserDetails();

        Integer currentUid = userDetails.getUid();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        String logPrefix = String.format("[UID:%d] ", currentUid);

        log.debug("{}>>>>>> Initiating getFilteredProposals API call: [Path: /proposal/list, Method: GET]", logPrefix);
        log.debug("{}Authenticated User Info: [UID: {}, Role: {}]", logPrefix, currentUid, role);
        log.debug("{}Received Request Parameters: [Summarize: {}, Expired: {}, SortBy: {}, SortOrder: {}, Page: {}, Limit: {}]",
                logPrefix, summarize, expired, sortBy, sortOrder, page, limit);

        Integer skip = (page - 1) * limit;

        ProposalFilteredListQueryRequestDto requestDto = ProposalFilteredListQueryRequestDto.builder()
                .summarize(summarize)
                .expired(expired)
                .sortOrder(sortOrder)
                .sortBy(sortBy)
                .skip(skip)
                .limit(limit)
                .build();

        GrpcProposalFilteredListQueryResult result = this.proposalFilteredListQueryProcessor.process(requestDto);

        return result.getSuccess()
                ? this.proposalFilteredListQueryProcessor.getSuccessResponseEntity(requestDto, result)
                : this.proposalFilteredListQueryProcessor.getFailureResponseEntity(result);
    }
}
