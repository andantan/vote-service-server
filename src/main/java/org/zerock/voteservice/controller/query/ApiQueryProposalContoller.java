package org.zerock.voteservice.controller.query;

import domain.event.proposal.query.protocol.GetProposalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.controller.mapper.ApiQueryEndpointMapper;
import org.zerock.voteservice.controller.query.processor.ProposalQueryProcessor;
import org.zerock.voteservice.dto.ResponseDto;
import org.zerock.voteservice.dto.query.ProposalQueryRequestDto;

@RestController
public class ApiQueryProposalContoller extends ApiQueryEndpointMapper {

    private final ProposalQueryProcessor processor;

    public ApiQueryProposalContoller(ProposalQueryProcessor processor) {
        this.processor = processor;
    }

    @GetMapping("/proposal/{topic}")
    public ResponseEntity<? extends ResponseDto> getProposal(
            @PathVariable(value = "topic") final String topic
    ) {
        ProposalQueryRequestDto dto = ProposalQueryRequestDto.builder()
                .topic(topic)
                .build();

        GetProposalResponse proposal = this.processor.getProposal(dto);

        if (!proposal.getQueried()) {
            return this.processor.getErrorResponse(proposal.getStatus());
        }

        return this.processor.getSuccessResponse(proposal.getStatus(), proposal.getProposals());
    }
}
