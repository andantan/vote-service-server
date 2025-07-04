package org.zerock.voteservice.adapter.in.web.processor.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalFilteredListQueryResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQueryFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQuerySuccessResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.ProposalResponseSchema;
import org.zerock.voteservice.adapter.in.web.processor.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalQueryProxy;

import java.util.List;

@Log4j2
@Service
public class ProposalFilteredListQueryProcessor implements Processor<
        ProposalFilteredListQueryRequestDto,
        GrpcProposalFilteredListQueryResult
        > {

    private final ProposalQueryProxy proxy;
    private final ProcessorHelper processorHelper;

    public ProposalFilteredListQueryProcessor(
            ProposalQueryProxy proxy,
            ProcessorHelper processorHelper
    ) {
        this.proxy = proxy;
        this.processorHelper = processorHelper;
    }

    @Override
    public GrpcProposalFilteredListQueryResult process(
            ProposalFilteredListQueryRequestDto dto
    ) {
        return this.proxy.getFilteredProposalList(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalFilteredListQueryRequestDto dto,
            GrpcProposalFilteredListQueryResult result
    ) {
        List<? extends ProposalResponseSchema> filteredProposalList = this.processorHelper.mappingFilteredProposalList(
                dto.getSummarize(), result.getGrpcResponseData().getProposalList()
        );

        ProposalFilteredListQuerySuccessResponseDto successDto = ProposalFilteredListQuerySuccessResponseDto
                .builder()
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
                .proposalList(filteredProposalList)
                .proposalListLength(filteredProposalList.size())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(
            GrpcProposalFilteredListQueryResult result
    ) {
        ProposalFilteredListQueryFailureResponseDto failureDto = ProposalFilteredListQueryFailureResponseDto
                .builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
