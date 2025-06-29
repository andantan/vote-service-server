package org.zerock.voteservice.adapter.in.web.controller.query.proposal.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.proposal.query.protocol.*;

import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalDetailRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalDetailResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalFilteredListRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.error.QueryErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.error.status.QueryProposalErrorStatus;
import org.zerock.voteservice.adapter.in.web.dto.query.proposal.QueryProposalFilteredListResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.schema.*;

import org.zerock.voteservice.adapter.out.grpc.proxy.query.ProposalQueryProxy;

import org.zerock.voteservice.tool.time.DateConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Log4j2
@Service
public class ProposalQueryProcessor {

    private final ProposalQueryProxy proposalQueryProxy;

    public ProposalQueryProcessor(ProposalQueryProxy proposalQueryProxy) {
        this.proposalQueryProxy = proposalQueryProxy;
    }

    public ProposalQueryProcessorResult validateTopic(String topic) {
        if (topic == null || topic.isEmpty()) {
            return ProposalQueryProcessorResult.failure("INVALID_PARAMETER");
        }

        return ProposalQueryProcessorResult.successWithoutData();
    }

    public ProposalQueryProcessorResult processProposalDetailQuery(QueryProposalDetailRequestDto dto) {
        GetProposalDetailResponse proposalDetail = this.proposalQueryProxy.getProposalDetail(dto);

        if (!proposalDetail.getQueried()) {
            return ProposalQueryProcessorResult.failure(proposalDetail.getStatus());
        }

        return ProposalQueryProcessorResult.success(proposalDetail.getStatus(), proposalDetail.getProposal());
    }

    public ProposalQueryProcessorResult processFilteredProposalsQuery(QueryProposalFilteredListRequestDto dto) {
        GetFilteredProposalListResponse filteredProposalList = this.proposalQueryProxy.getFilteredProposalList(dto);

        if (!filteredProposalList.getQueried()) {
            return ProposalQueryProcessorResult.failure(filteredProposalList.getStatus());
        }

        return ProposalQueryProcessorResult.successForDetailList(filteredProposalList.getStatus(), filteredProposalList.getProposalListList());
    }

    public ResponseEntity<QueryProposalDetailResponseDto> getSuccessResponse(QueryProposalDetailRequestDto dto, ProposalQueryProcessorResult result) {
        ProposalDetailSchema proposalDetailSchema = this.mappingProposalDetailSchema(result.getProposal());

        QueryProposalDetailResponseDto successDto = QueryProposalDetailResponseDto.builder()
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .topic(dto.getTopic())
                .proposal(proposalDetailSchema)
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<QueryProposalFilteredListResponseDto> getSuccessResponse(QueryProposalFilteredListRequestDto dto, ProposalQueryProcessorResult result) {
        List<? extends ProposalResponseSchema> proposalList = this.mappingProposalResponseSchema(dto, result);

        QueryProposalFilteredListResponseDto successDto = QueryProposalFilteredListResponseDto.builder()
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .summarize(dto.getSummarize())
                .expired(dto.getExpired())
                .sortOrder(dto.getSortOrder())
                .sortBy(dto.getSortBy())
                .skip(dto.getSkip())
                .limit(dto.getLimit())
                .proposalList(proposalList)
                .proposalListLength(proposalList.size())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(ProposalQueryProcessorResult result) {
        QueryProposalErrorStatus errorStatus = QueryProposalErrorStatus.fromCode(result.getStatus());
        QueryErrorResponseDto errorDto = QueryErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }

    private List<? extends ProposalResponseSchema> mappingProposalResponseSchema(QueryProposalFilteredListRequestDto dto, ProposalQueryProcessorResult result) {
        Function<Proposal, ? extends ProposalResponseSchema> mapper = dto.getSummarize()
                ? this::mappingProposalSummarizedSchema
                : this::mappingProposalDetailSchema;

        return result.getProposalList().stream()
                .map(mapper)
                .toList();
    }

    private ProposalSummarizedSchema mappingProposalSummarizedSchema(Proposal proposal) {
        LocalDateTime kstExpiredAt = DateConverter.toKstLocalDateTime(proposal.getExpiredAt());

        return ProposalSummarizedSchema.builder()
                .topic(proposal.getTopic())
                .expired(proposal.getExpired())
                .expiredAt(kstExpiredAt)
                .build();
    }

    private ProposalDetailSchema mappingProposalDetailSchema(Proposal proposal) {
        List<BlockHeightSchema> blockHeightSchemas = proposal.getBlockHeightsList().stream()
                .map(this::mappingBlockHeightSchema)
                .toList();

        ResultSchema resultSchema = this.mappingResultSchema(proposal.getResult());

        LocalDateTime kstCreatedAt = DateConverter.toKstLocalDateTime(proposal.getCreatedAt());
        LocalDateTime kstExpiredAt = DateConverter.toKstLocalDateTime(proposal.getExpiredAt());

        List<String> options = proposal.getOptionsList();

        return ProposalDetailSchema.builder()
                .topic(proposal.getTopic())
                .duration(proposal.getDuration())
                .expired(proposal.getExpired())
                .blockHeights(blockHeightSchemas)
                .result(resultSchema)
                .createdAt(kstCreatedAt)
                .expiredAt(kstExpiredAt)
                .options(options)
                .build();
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
