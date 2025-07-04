package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotTransactionResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotTransactionFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotTransactionRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotTransactionSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotCreateProxy;

@Log4j2
@Service
public class BallotTransactionProcessor implements Processor<
        BallotTransactionRequestDto,
        GrpcBallotTransactionResponseResult
        > {

    private final BallotCreateProxy proxy;

    public BallotTransactionProcessor(BallotCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcBallotTransactionResponseResult execute(
            BallotTransactionRequestDto dto
    ) {
        return this.proxy.submitBallotTransaction(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            BallotTransactionRequestDto dto,
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

    @Override
    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(
            GrpcBallotTransactionResponseResult result
    ) {
        BallotTransactionFailureResponseDto failureDto = BallotTransactionFailureResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
