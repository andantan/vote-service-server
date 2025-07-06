package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotCachingResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotCachingGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.response.BallotCachingSuccessResponseDto;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotCreateProxy;

@Log4j2
@Service
public class BallotCachingProcessor implements Processor<
        BallotCachingGrpcRequestDto,
        GrpcBallotCachingResponseResult
        > {

    private final BallotCreateProxy proxy;

    public BallotCachingProcessor(BallotCreateProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public GrpcBallotCachingResponseResult execute(
            BallotCachingGrpcRequestDto dto
    ) {
        return this.proxy.cacheBallot(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            BallotCachingGrpcRequestDto dto,
            GrpcBallotCachingResponseResult result
    ) {
        BallotCachingSuccessResponseDto successDto = BallotCachingSuccessResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .userHash(dto.getUserHash())
                .voteHash(dto.getVoteHash())
                .topic(dto.getTopic())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }
}
