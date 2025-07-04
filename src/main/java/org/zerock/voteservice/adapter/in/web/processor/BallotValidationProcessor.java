package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotValidationResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotValidationFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotValidationRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotValidationSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotCreateProxy;

@Log4j2
@Service
public class BallotValidationProcessor implements Processor<
        BallotValidationRequestDto,
        GrpcBallotValidationResponseResult
        > {

    private final BallotCreateProxy proxy;

    public BallotValidationProcessor(BallotCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcBallotValidationResponseResult execute(
            BallotValidationRequestDto dto
    ) {
        return this.proxy.validateBallot(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            BallotValidationRequestDto dto,
            GrpcBallotValidationResponseResult result
    ) {
        BallotValidationSuccessResponseDto successDto = BallotValidationSuccessResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .userHash(dto.getUserHash())
                .topic(dto.getTopic())
                .option(dto.getOption())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(
            GrpcBallotValidationResponseResult result
    ) {
        BallotValidationFailureResponseDto failureDto = BallotValidationFailureResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
