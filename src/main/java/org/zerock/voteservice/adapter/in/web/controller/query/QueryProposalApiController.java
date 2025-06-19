package org.zerock.voteservice.adapter.in.web.controller.query;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.controller.query.processor.ProposalQueryResult;
import org.zerock.voteservice.adapter.in.web.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.query.docs.QueryProposalApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.query.processor.ProposalQueryProcessor;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryProposalDetailRequestDto;

@RestController
public class QueryProposalApiController extends QueryApiEndpointMapper {

    private final ProposalQueryProcessor proposalQueryProcessor;

    public QueryProposalApiController(ProposalQueryProcessor proposalQueryProcessor) {
        this.proposalQueryProcessor = proposalQueryProcessor;
    }

    @QueryProposalApiDoc
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

    //@GetMapping("/proposal/list")
    //public ResponseEntity<? extends ResponseDto> getFilteredProposals
}
