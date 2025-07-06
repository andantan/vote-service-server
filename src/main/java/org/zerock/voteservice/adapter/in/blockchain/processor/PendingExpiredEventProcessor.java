package org.zerock.voteservice.adapter.in.blockchain.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc.PendingExpiredEventGrpcRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.response.PendingExpiredEventSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.BlockchainEventProxy;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcPendingExpiredEventResponseResult;

@Log4j2
@Component
@RequiredArgsConstructor
public class PendingExpiredEventProcessor implements Processor<
        PendingExpiredEventGrpcRequestDto,
        GrpcPendingExpiredEventResponseResult
        > {

    private final BlockchainEventProxy proxy;

    @Override
    public GrpcPendingExpiredEventResponseResult execute(
            PendingExpiredEventGrpcRequestDto dto
    ) {
        return proxy.reportPendingExpiredEvent(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            PendingExpiredEventGrpcRequestDto dto,
            GrpcPendingExpiredEventResponseResult result
    ) {
        PendingExpiredEventSuccessResponseDto successDto = PendingExpiredEventSuccessResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .voteId(dto.getVoteId())
                .voteCount(dto.getVoteCount())
                .voteOptions(dto.getVoteOptions())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }
}
