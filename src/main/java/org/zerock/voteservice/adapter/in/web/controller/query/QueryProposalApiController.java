package org.zerock.voteservice.adapter.in.web.controller.query;

import domain.event.proposal.query.protocol.GetProposalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        QueryProposalDetailRequestDto dto = QueryProposalDetailRequestDto.builder()
                .topic(topic)
                .build();

        GetProposalResponse proposal = this.proposalQueryProcessor.getProposal(dto);

        if (!proposal.getQueried()) {
            return this.proposalQueryProcessor.getErrorResponse(proposal.getStatus());
        }

        return this.proposalQueryProcessor.getSuccessResponse(proposal.getStatus(), proposal.getProposals());
    }

    @GetMapping("/proposal/list")
    public ResponseEntity<? extends ResponseDto> getFilteredProposals (
            @RequestParam(value = "summarize") final Boolean summarize

    ) {
        return null;
    }
}
