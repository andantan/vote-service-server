package org.zerock.voteservice.adapter.in.web.controller.query.proposal;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.zerock.voteservice.adapter.in.web.controller.query.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.query.proposal.docs.QueryProposalDetailApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.query.proposal.docs.QueryProposalFilteredListApiDoc;

import org.zerock.voteservice.adapter.in.web.controller.query.proposal.processor.ProposalQueryResult;
import org.zerock.voteservice.adapter.in.web.controller.query.proposal.processor.ProposalQueryProcessor;

import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalDetailRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalFilteredListRequestDto;

@Log4j2
@RestController
public class QueryProposalApiController extends QueryApiEndpointMapper {

    private final ProposalQueryProcessor proposalQueryProcessor;

    public QueryProposalApiController(ProposalQueryProcessor proposalQueryProcessor) {
        this.proposalQueryProcessor = proposalQueryProcessor;
    }

    @QueryProposalDetailApiDoc
    @GetMapping("/proposal/{topic}/detail")
    public ResponseEntity<? extends ResponseDto> getProposalDetail(
            @PathVariable(value = "topic") final String topic
    ) {
        ProposalQueryResult topicValidationResult = this.proposalQueryProcessor.validateTopic(topic);

        if (!topicValidationResult.getSuccess()) {
            return this.proposalQueryProcessor.getErrorResponse(topicValidationResult);
        }

        QueryProposalDetailRequestDto dto = QueryProposalDetailRequestDto.builder()
                .topic(topic)
                .build();

        ProposalQueryResult result = this.proposalQueryProcessor.processProposalDetailQuery(dto);

        if (!result.getSuccess()) {
            return this.proposalQueryProcessor.getErrorResponse(result);
        }

        return this.proposalQueryProcessor.getSuccessResponse(dto, result);
    }

    @QueryProposalFilteredListApiDoc
    @GetMapping("/proposal/list")
    public ResponseEntity<? extends ResponseDto> getFilteredProposals(
            @RequestParam(name = "summarize", defaultValue = "false") Boolean summarize,
            @RequestParam(name = "expired", required = false) Boolean expired,
            @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder,
            @RequestParam(name = "sortBy", defaultValue = "expiredAt") String sortBy,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "limit", defaultValue = "15") Integer limit
    ) {
        Integer skip = (page - 1) * limit;

        QueryProposalFilteredListRequestDto dto = QueryProposalFilteredListRequestDto.builder()
                .summarize(summarize)
                .expired(expired)
                .sortOrder(sortOrder)
                .sortBy(sortBy)
                .skip(skip)
                .limit(limit)
                .build();

        ProposalQueryResult result = this.proposalQueryProcessor.processFilteredProposalsQuery(dto);

        if (!result.getSuccess()) {
            return this.proposalQueryProcessor.getErrorResponse(result);
        }

        return this.proposalQueryProcessor.getSuccessResponse(dto, result);
    }
}
