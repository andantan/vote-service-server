package org.zerock.voteservice.adapter.in.web.processor.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcProposalCachingResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalCachingFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalCachingRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.ProposalCachingSuccessResponseDto;
import org.zerock.voteservice.adapter.in.web.processor.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalCreateProxy;

@Log4j2
@Service
public class ProposalCachingProcessor implements Processor<
        ProposalCachingRequestDto,
        GrpcProposalCachingResponseResult
        > {

    private final ProposalCreateProxy proxy;

    public ProposalCachingProcessor(ProposalCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcProposalCachingResponseResult execute(
            ProposalCachingRequestDto dto
    ) {
        return this.proxy.cachingProposal(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalCachingRequestDto dto,
            GrpcProposalCachingResponseResult result
    ) {
        ProposalCachingSuccessResponseDto successDto = ProposalCachingSuccessResponseDto.builder()
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .topic(dto.getTopic())
                .duration(dto.getDuration())
                .options(dto.getOptions())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(
            GrpcProposalCachingResponseResult result
    ) {
        ProposalCachingFailureResponseDto failureDto = ProposalCachingFailureResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
