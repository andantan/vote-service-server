package org.zerock.voteservice.adapter.in.web.controller.query.processor;

import domain.event.proposal.query.protocol.BlockHeight;
import domain.event.proposal.query.protocol.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.proposal.query.protocol.Proposal;
import domain.event.proposal.query.protocol.GetProposalResponse;

import org.zerock.voteservice.adapter.in.web.dto.query.QueryProposalDetailRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryProposalDetailResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.error.QueryProposalErrorStatus;
import org.zerock.voteservice.adapter.in.web.dto.query.schema.BlockHeightSchema;
import org.zerock.voteservice.adapter.in.web.dto.query.schema.ResultSchema;
import org.zerock.voteservice.adapter.out.grpc.proxy.query.ProposalQueryProxy;
import org.zerock.voteservice.tool.date.Converter;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class ProposalQueryProcessor {

    private final ProposalQueryProxy proposalQueryProxy;

    public ProposalQueryProcessor(ProposalQueryProxy proposalQueryProxy) {
        this.proposalQueryProxy = proposalQueryProxy;
    }

    public ProposalQueryResult validateTopic(String topic) {
        if (topic == null || topic.isEmpty()) {
            return ProposalQueryResult.failure("INVALID_PARAMETER");
        }

        return ProposalQueryResult.successWithoutData();
    }

    public ProposalQueryResult processProposalDetailQuery(QueryProposalDetailRequestDto dto) {
        GetProposalResponse proposalDetail = this.proposalQueryProxy.getProposal(dto);

        if (!proposalDetail.getQueried()) {
            return ProposalQueryResult.failure(proposalDetail.getStatus());
        }

        return ProposalQueryResult.success(proposalDetail.getStatus(), proposalDetail.getProposals());
    }

    public ResponseEntity<QueryProposalDetailResponseDto> getSuccessResponse(QueryProposalDetailRequestDto dto, ProposalQueryResult result) {
        Proposal proposal = result.getProposal();

        List<BlockHeightSchema> blockHeightSchemas = proposal.getBlockHeightsList().stream()
                .map(this::mappingBlockHeightSchema)
                .toList();

        ResultSchema resultSchema = this.mappingResultSchema(proposal.getResult());

        LocalDateTime kstCreatedAt = Converter.toKstLocalDateTime(proposal.getCreatedAt());
        LocalDateTime kstExpiredAt = Converter.toKstLocalDateTime(proposal.getExpiredAt());

        List<String> options = proposal.getOptionsList();

        QueryProposalDetailResponseDto successDto = QueryProposalDetailResponseDto.builder()
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .topic(dto.getTopic())
                .duration(proposal.getDuration())
                .expired(proposal.getExpired())
                .blockHeights(blockHeightSchemas)
                .result(resultSchema)
                .createdAt(kstCreatedAt)
                .expiredAt(kstExpiredAt)
                .options(options)
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(ProposalQueryResult result) {
        QueryProposalErrorStatus errorStatus = QueryProposalErrorStatus.fromCode(result.getStatus());
        QueryErrorResponseDto errorDto = QueryErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }

    private BlockHeightSchema mappingBlockHeightSchema(BlockHeight blockHeight) {
        return BlockHeightSchema.builder()
                .height(blockHeight.getHeight())
                .length(blockHeight.getLength())
                .build();
    }

    private ResultSchema mappingResultSchema(Result result) {
        return ResultSchema.builder()
                .count(result.getCount())
                .options(result.getOptionsMap())
                .build();
    }
}
