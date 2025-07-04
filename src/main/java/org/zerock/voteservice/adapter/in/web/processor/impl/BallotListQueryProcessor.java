package org.zerock.voteservice.adapter.in.web.processor.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.domain.data.impl.GrpcBallotListQueryResponseResult;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotListQueryFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotListQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotListQuerySuccessResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.schema.BallotSchema;
import org.zerock.voteservice.adapter.in.web.processor.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotQueryProxy;

import java.util.List;

@Log4j2
@Service
public class BallotListQueryProcessor implements Processor<
        BallotListQueryRequestDto,
        GrpcBallotListQueryResponseResult
        > {

    private final ProcessorHelper processorHelper;
    private final BallotQueryProxy proxy;

    public BallotListQueryProcessor(
            ProcessorHelper processorHelper,
            BallotQueryProxy proxy
    ) {
        this.processorHelper = processorHelper;
        this.proxy = proxy;
    }

    @Override
    public GrpcBallotListQueryResponseResult execute(
            BallotListQueryRequestDto dto
    ) {
        return this.proxy.getBallotList(dto);
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getSuccessResponseEntity(
            BallotListQueryRequestDto dto,
            GrpcBallotListQueryResponseResult result
    ) {
        List<BallotSchema> ballotSchemas = result.getGrpcResponseData().getBallotList().stream()
                .map(this.processorHelper::mapToBallotSchema)
                .toList();

        BallotListQuerySuccessResponseDto successDto = BallotListQuerySuccessResponseDto.builder()
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .userHash(dto.getUserHash())
                .ballotList(ballotSchemas)
                .ballotListLength(ballotSchemas.size())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    @Override
    public ResponseEntity<? extends ResponseDto> getFailureResponseEntity(
            GrpcBallotListQueryResponseResult result
    ) {
        BallotListQueryFailureResponseDto failureDto = BallotListQueryFailureResponseDto.builder()
                .success(result.getSuccess())
                .status(result.getStatus())
                .message(result.getMessage())
                .httpStatusCode(result.getHttpStatusCode())
                .build();

        return new ResponseEntity<>(failureDto, HttpStatus.valueOf(failureDto.getHttpStatusCode()));
    }
}
