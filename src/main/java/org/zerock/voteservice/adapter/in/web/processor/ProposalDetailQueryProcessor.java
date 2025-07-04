package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.processor.helper.ProposalQueryProcessHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalDetailQueryResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQueryFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalDetailQuerySuccessResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.schema.ProposalDetailSchema;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalQueryProxy;

@Log4j2
@Service
public class ProposalDetailQueryProcessor implements Processor<
        ProposalDetailQueryRequestDto,
        GrpcProposalDetailQueryResponseResult
        > {

    private final ProposalQueryProxy proxy;
    private final ProposalQueryProcessHelper helper;

    public ProposalDetailQueryProcessor(
            ProposalQueryProxy proxy,
            ProposalQueryProcessHelper helper
    ) {
        this.proxy = proxy;
        this.helper = helper;
    }

    @Override
    public GrpcProposalDetailQueryResponseResult execute(
            ProposalDetailQueryRequestDto dto
    ) {
        return this.proxy.getProposalDetail(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalDetailQueryRequestDto dto,
            GrpcProposalDetailQueryResponseResult result
    ) {
        ProposalDetailSchema proposalDetailSchema = this.helper.mapToProposalDetailSchema(
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
            GrpcProposalDetailQueryResponseResult result
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
