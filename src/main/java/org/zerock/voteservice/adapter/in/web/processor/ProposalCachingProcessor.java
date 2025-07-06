package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalCachingResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalCachingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.ProposalCachingSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalCreateProxy;

@Log4j2
@Service
public class ProposalCachingProcessor implements Processor<
        ProposalCachingGrpcRequestDto,
        GrpcProposalCachingResponseResult
        > {

    private final ProposalCreateProxy proxy;

    public ProposalCachingProcessor(ProposalCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcProposalCachingResponseResult execute(
            ProposalCachingGrpcRequestDto dto
    ) {
        return this.proxy.cachingProposal(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalCachingGrpcRequestDto dto,
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
}
