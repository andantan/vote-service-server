package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.processor.helper.ProposalQueryProcessHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalFilteredListQueryResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQueryFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalFilteredListQuerySuccessResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.schema.ProposalResponseSchema;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalQueryProxy;

import java.util.List;

@Log4j2
@Service
public class ProposalFilteredListQueryProcessor implements Processor<
        ProposalFilteredListQueryRequestDto,
        GrpcProposalFilteredListQueryResponseResult
        > {

    private final ProposalQueryProxy proxy;
    private final ProposalQueryProcessHelper helper;

    public ProposalFilteredListQueryProcessor(
            ProposalQueryProxy proxy,
            ProposalQueryProcessHelper helper
    ) {
        this.proxy = proxy;
        this.helper = helper;
    }

    @Override
    public GrpcProposalFilteredListQueryResponseResult execute(
            ProposalFilteredListQueryRequestDto dto
    ) {
        return this.proxy.getFilteredProposalList(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalFilteredListQueryRequestDto dto,
            GrpcProposalFilteredListQueryResponseResult result
    ) {
        List<? extends ProposalResponseSchema> filteredProposalList = this.helper.mapToFilteredProposalList(
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
            GrpcProposalFilteredListQueryResponseResult result
    ) {
        ProposalFilteredListQueryFailureResponseDto failureDto = ProposalFilteredListQueryFailureResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
