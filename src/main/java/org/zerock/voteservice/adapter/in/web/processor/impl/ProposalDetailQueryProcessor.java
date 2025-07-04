package org.zerock.voteservice.adapter.in.web.processor.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalDetailQueryResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQueryFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQuerySuccessResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.ProposalDetailSchema;
import org.zerock.voteservice.adapter.in.web.processor.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalQueryProxy;

@Log4j2
@Service
public class ProposalDetailQueryProcessor implements Processor<
        ProposalDetailQueryRequestDto,
        GrpcProposalDetailQueryResult
        > {

    private final ProposalQueryProxy proxy;
    private final ProcessorHelper processorHelper;

    public ProposalDetailQueryProcessor(
            ProposalQueryProxy proxy,
            ProcessorHelper processorHelper
    ) {
        this.proxy = proxy;
        this.processorHelper = processorHelper;
    }

    public GrpcProposalDetailQueryResult process(
            ProposalDetailQueryRequestDto dto
    ) {
        return this.proxy.getProposalDetail(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalDetailQueryRequestDto dto,
            GrpcProposalDetailQueryResult result
    ) {
        ProposalDetailSchema proposalDetailSchema = this.processorHelper.mappingProposalDetailSchema(
                result.getGrpcResponseData().getProposal()
        );

        ProposalDetailQuerySuccessResponseDto successDto = ProposalDetailQuerySuccessResponseDto
                .builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .topic(dto.getTopic())
                .proposal(proposalDetailSchema)
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(
            GrpcProposalDetailQueryResult result
    ) {
        ProposalDetailQueryFailureResponseDto failureDto = ProposalDetailQueryFailureResponseDto
                .builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));

    }
}
