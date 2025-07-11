package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalPendingResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalPendingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.ProposalPendingSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.ProposalCreateProxy;

@Log4j2
@Service
public class ProposalPendingProcessor implements Processor<
        ProposalPendingGrpcRequestDto,
        GrpcProposalPendingResponseResult
        > {

    private final ProposalCreateProxy proxy;

    public ProposalPendingProcessor(ProposalCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcProposalPendingResponseResult execute(
            ProposalPendingGrpcRequestDto dto
    ) {
        return this.proxy.pendingProposal(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            ProposalPendingGrpcRequestDto dto,
            GrpcProposalPendingResponseResult result
    ) {
        ProposalPendingSuccessResponseDto successDto = ProposalPendingSuccessResponseDto.builder()
                .topic(dto.getTopic())
                .duration(dto.getDuration())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }
}
