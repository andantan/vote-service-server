package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotTransactionResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotTransactionGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.BallotTransactionSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotCreateProxy;

@Log4j2
@Service
public class BallotTransactionProcessor implements Processor<
        BallotTransactionGrpcRequestDto,
        GrpcBallotTransactionResponseResult
        > {

    private final BallotCreateProxy proxy;

    public BallotTransactionProcessor(BallotCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcBallotTransactionResponseResult execute(
            BallotTransactionGrpcRequestDto dto
    ) {
        return this.proxy.submitBallotTransaction(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            BallotTransactionGrpcRequestDto dto,
            GrpcBallotTransactionResponseResult result
    ) {
        BallotTransactionSuccessResponseDto successDto = BallotTransactionSuccessResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .userHash(dto.getUserHash())
                .topic(dto.getTopic())
                .option(dto.getOption())
                .voteHash(result.getVoteHash())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }
}
