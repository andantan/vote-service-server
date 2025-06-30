package org.zerock.voteservice.adapter.in.web.controller.query.proposal;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.zerock.voteservice.adapter.in.web.controller.query.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.query.proposal.docs.QueryProposalDetailApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.query.proposal.docs.QueryProposalFilteredListApiDoc;

import org.zerock.voteservice.adapter.in.web.controller.query.proposal.processor.ProposalQueryProcessorResult;
import org.zerock.voteservice.adapter.in.web.controller.query.proposal.processor.ProposalQueryProcessor;

import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalDetailRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalFilteredListRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.user.authentication.UserAuthenticationDetails;

@Log4j2
@RestController
public class QueryProposalApiController extends QueryApiEndpointMapper {

    private final ProposalQueryProcessor proposalQueryProcessor;

    public QueryProposalApiController(ProposalQueryProcessor proposalQueryProcessor) {
        this.proposalQueryProcessor = proposalQueryProcessor;
    }

    @QueryProposalDetailApiDoc
    @GetMapping("/proposal/{topic}/detail")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<? extends ResponseDto> getProposalDetail(
            @PathVariable(value = "topic") final String topic
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        Integer currentUid = userDetails.getUid();
        String logPrefix = String.format("[UID:%d] ", currentUid);

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        log.debug("{}>>>>>> Initiating getProposalDetail API call: [Path: /proposal/{}/detail, Method: GET]", logPrefix, topic);
        log.debug("{}Authenticated User Info: [Username: {}, Role: {}]", logPrefix, userDetails.getUsername(), role);
        log.debug("{}Received Path Variable: [Topic: {}]", logPrefix, topic);

        try {
            ProposalQueryProcessorResult topicValidationResult = this.proposalQueryProcessor.validateTopic(topic);

            if (!topicValidationResult.getSuccess()) {
                log.warn("{}Proposal topic validation failed: [Topic: {}, Status: {}, Message: {}]",
                        logPrefix, topic, topicValidationResult.getStatus(), topicValidationResult.getMessage());

                return this.proposalQueryProcessor.getErrorResponse(topicValidationResult);
            }

            QueryProposalDetailRequestDto dto = QueryProposalDetailRequestDto.builder()
                    .topic(topic)
                    .build();

            ProposalQueryProcessorResult result = this.proposalQueryProcessor.processProposalDetailQuery(dto);

            if (!result.getSuccess()) {
                log.warn("{}Proposal detail query processing failed: [Topic: {}, Status: {}, Message: {}]",
                        logPrefix, topic, result.getStatus(), result.getMessage());

                return this.proposalQueryProcessor.getErrorResponse(result);
            }

            log.info("{}Proposal detail query successful: [Topic: {}, Status: {}]",
                    logPrefix, topic, result.getStatus());

            return this.proposalQueryProcessor.getSuccessResponse(dto, result);
        } catch (StatusRuntimeException e) {
            Status.Code statusCode = e.getStatus().getCode();
            String statusDescription = e.getStatus().getDescription();

            if (statusCode == Status.Code.UNAVAILABLE) {
                log.error("{}gRPC Server is unavailable or unreachable for proposal detail query. Check server status and network. Error: {}", logPrefix, statusDescription);
            } else if (statusCode == Status.Code.DEADLINE_EXCEEDED) {
                log.error("{}gRPC call timed out for proposal detail query. Consider increasing timeout or checking server performance. Error: {}", logPrefix, statusDescription);
            } else {
                log.error("{}Unexpected gRPC error during proposal detail query: Status={}, Description={}", logPrefix, statusCode, statusDescription, e);
            }

            return this.proposalQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");

        } catch (RuntimeException e) {
            log.error("{}{}", logPrefix, e.getMessage());
            return this.proposalQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        } catch (Exception e) {
            log.error("{}An unexpected error occurred during proposal detail query for topic: {}. Error: {}", logPrefix, topic, e.getMessage(), e);
            return this.proposalQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }

    @QueryProposalFilteredListApiDoc
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthenticationDetails userDetails = (UserAuthenticationDetails) authentication.getPrincipal();

        Integer currentUid = userDetails.getUid();
        String logPrefix = String.format("[UID:%d] ", currentUid);

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(java.util.stream.Collectors.joining(", "));

        log.debug("{}>>>>>> Initiating getFilteredProposals API call: [Path: /proposal/list, Method: GET]", logPrefix);
        log.debug("{}Authenticated User Info: [UID: {}, Role: {}]", logPrefix, currentUid, role);
        log.debug("{}Received Request Parameters: [Summarize: {}, Expired: {}, SortBy: {}, SortOrder: {}, Page: {}, Limit: {}]",
                logPrefix, summarize, expired, sortBy, sortOrder, page, limit);

        Integer skip = (page - 1) * limit;

        QueryProposalFilteredListRequestDto dto = QueryProposalFilteredListRequestDto.builder()
                .summarize(summarize)
                .expired(expired)
                .sortOrder(sortOrder)
                .sortBy(sortBy)
                .skip(skip)
                .limit(limit)
                .build();

        try {
            ProposalQueryProcessorResult result = this.proposalQueryProcessor.processFilteredProposalsQuery(dto);

            if (!result.getSuccess()) {
                log.warn("{}Filtered proposals query processing failed: [Status: {}]",
                        logPrefix, result.getStatus());

                return this.proposalQueryProcessor.getErrorResponse(result);
            }

            log.info("{}Filtered proposals query successful: [Status: {}, Found {} proposals]",
                    logPrefix, result.getStatus(), result.getProposalList() != null ? result.getProposalList().size() : 0);

            return this.proposalQueryProcessor.getSuccessResponse(dto, result);
        } catch (StatusRuntimeException e) {
            // gRPC 통신 오류 처리
            Status.Code statusCode = e.getStatus().getCode();
            String statusDescription = e.getStatus().getDescription();

            if (statusCode == Status.Code.UNAVAILABLE) {
                log.error("{}gRPC Server is unavailable or unreachable for filtered proposals query. Check server status and network. Error: {}", logPrefix, statusDescription);
            } else if (statusCode == Status.Code.DEADLINE_EXCEEDED) {
                log.error("{}gRPC call timed out for filtered proposals query. Consider increasing timeout or checking server performance. Error: {}", logPrefix, statusDescription);
            } else {
                log.error("{}Unexpected gRPC error during filtered proposals query: Status={}, Description={}", logPrefix, statusCode, statusDescription, e);
            }

            return this.proposalQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");

        } catch (Exception e) {
            log.error("{}An unexpected error occurred during filtered proposals query. Error: {}", logPrefix, e.getMessage(), e);
            return this.proposalQueryProcessor.getErrorResponse("INTERNAL_SERVER_ERROR");
        }
    }
}
