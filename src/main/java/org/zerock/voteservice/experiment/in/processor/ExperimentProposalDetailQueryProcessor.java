package org.zerock.voteservice.experiment.in.processor;

import domain.event.proposal.query.protocol.BlockHeight;
import domain.event.proposal.query.protocol.Proposal;
import domain.event.proposal.query.protocol.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.BlockHeightSchema;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.ProposalDetailSchema;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.ResultSchema;
import org.zerock.voteservice.experiment.in.domain.data.ExperimentProposalDetailQueryGrpcResult;
import org.zerock.voteservice.experiment.in.domain.dto.*;
import org.zerock.voteservice.experiment.out.proxy.ExperimentProposalQueryProxy;
import org.zerock.voteservice.tool.time.DateConverter;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class ExperimentProposalDetailQueryProcessor implements ExperimentProcessor<
        ExperimentProposalDetailQueryRequestDto,
        ExperimentProposalDetailQueryGrpcResult
        > {

    private final ExperimentProposalQueryProxy experimentProxy;

    public ExperimentProposalDetailQueryProcessor(ExperimentProposalQueryProxy experimentProxy) {
        this.experimentProxy = experimentProxy;
    }

    public ExperimentProposalDetailQueryGrpcResult getProposalDetail(
            ExperimentProposalDetailQueryRequestDto dto
    ) {
        return this.experimentProxy.getProposalDetail(dto);
    }

    @Override
    public ResponseEntity<? extends ExperimentResponseDto> getSuccessResponseEntity(
            ExperimentProposalDetailQueryRequestDto dto,
            ExperimentProposalDetailQueryGrpcResult result
    ) {
        ProposalDetailSchema proposalDetailSchema = this.mappingProposalDetailSchema(result.getExperimentGrpcResponseData().getProposal());

        ExperimentProposalDetailQuerySuccessResponseDto successDto = ExperimentProposalDetailQuerySuccessResponseDto
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
    public ResponseEntity<? extends ExperimentResponseDto> getfailureResponseEntity(
            ExperimentProposalDetailQueryGrpcResult result
    ) {
        ExperimentProposalDetailQueryFailureResponseDto errorDto = ExperimentProposalDetailQueryFailureResponseDto
                .builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
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
