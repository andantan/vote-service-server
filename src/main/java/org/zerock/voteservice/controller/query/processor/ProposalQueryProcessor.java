package org.zerock.voteservice.controller.query.processor;

import domain.event.proposal.query.protocol.BlockHeight;
import domain.event.proposal.query.protocol.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.proposal.query.protocol.Proposal;
import domain.event.proposal.query.protocol.GetProposalResponse;

import org.zerock.voteservice.dto.query.ProposalQueryRequestDto;
import org.zerock.voteservice.dto.query.ProposalQueryResponseDto;
import org.zerock.voteservice.dto.query.QueryErrorResponseDto;
import org.zerock.voteservice.dto.query.error.ProposalQueryErrorStatus;
import org.zerock.voteservice.dto.query.schema.BlockHeightSchema;
import org.zerock.voteservice.dto.query.schema.ResultSchema;
import org.zerock.voteservice.grpc.event.GrpcProposalQueryEventClient;
import org.zerock.voteservice.tool.date.Converter;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
public class ProposalQueryProcessor {

    private final GrpcProposalQueryEventClient grpcEventClient;

    public ProposalQueryProcessor(
            GrpcProposalQueryEventClient grpcEventClient
    ) {
        this.grpcEventClient = grpcEventClient;
    }

    public GetProposalResponse getProposal(ProposalQueryRequestDto dto) {
        return this.grpcEventClient.getProposal(dto.getTopic());
    }

    public ResponseEntity<ProposalQueryResponseDto> getSuccessResponse(String internalStatus, Proposal proposal) {
        String successMessage = "조회가 완료되었습니다.";

        List<BlockHeightSchema> blockHeightSchemas = proposal.getBlockHeightsList().stream()
                .map(this::mappingBlockHeightSchema)
                .toList();

        ResultSchema resultSchema = this.mappingResultSchema(proposal.getResult());

        LocalDateTime kstCreatedAt = Converter.toKstLocalDateTime(proposal.getCreatedAt());
        LocalDateTime kstExpiredAt = Converter.toKstLocalDateTime(proposal.getExpiredAt());

        List<String> options = proposal.getOptionsList();

        ProposalQueryResponseDto successDto = ProposalQueryResponseDto.builder()
                .success(true)
                .message(successMessage)
                .status(internalStatus)
                .httpStatusCode(HttpStatus.OK.value())
                .topic(proposal.getTopic())
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

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(String internalStatus) {
        ProposalQueryErrorStatus errorStatus = ProposalQueryErrorStatus.fromCode(internalStatus);
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
