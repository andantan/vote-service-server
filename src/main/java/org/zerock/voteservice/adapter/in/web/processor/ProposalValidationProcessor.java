package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalValidationResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalValidationRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.ProposalValidationSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalCreateProxy;

@Log4j2
@Service
public class ProposalValidationProcessor implements Processor<
        ProposalValidationRequestDto,
        GrpcProposalValidationResponseResult
        > {

    private final ProposalCreateProxy proxy;

    public ProposalValidationProcessor(ProposalCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcProposalValidationResponseResult execute(ProposalValidationRequestDto dto) {
        return this.proxy.validateProposal(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalValidationRequestDto dto,
            GrpcProposalValidationResponseResult result
    ) {
        ProposalValidationSuccessResponseDto successDto = ProposalValidationSuccessResponseDto.builder()
                .topic(dto.getTopic())
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }
}
