package org.zerock.voteservice.adapter.in.blockchain.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc.BlockCreatedEventGrpcRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.response.BlockCreatedEventSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.out.grpc.proxy.BlockchainEventProxy;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBlockCreatedEventResponseResult;

@Log4j2
@Service
@RequiredArgsConstructor
public class BlockCreatedEventProcessor implements Processor<
        BlockCreatedEventGrpcRequestDto,
        GrpcBlockCreatedEventResponseResult
        > {

    private final BlockchainEventProxy proxy;

    @Override
    public GrpcBlockCreatedEventResponseResult execute(
            BlockCreatedEventGrpcRequestDto dto
    ) {
        return proxy.reportBlockCreatedEvent(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            BlockCreatedEventGrpcRequestDto dto,
            GrpcBlockCreatedEventResponseResult result
    ) {
        BlockCreatedEventSuccessResponseDto successDto = BlockCreatedEventSuccessResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .topic(dto.getTopic())
                .txCount(dto.getTxCount())
                .height(dto.getHeight())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }
}
