package org.zerock.voteservice.adapter.in.web.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.web.processor.helper.BallotQueryProcessorHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotListQueryResponseResult;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotListQueryFailureResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotListQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.impl.BallotListQuerySuccessResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.schema.BallotSchema;
import org.zerock.voteservice.adapter.in.common.Processor;
import org.zerock.voteservice.adapter.out.grpc.proxy.BallotQueryProxy;

import java.util.List;

@Log4j2
@Service
public class BallotListQueryProcessor implements Processor<
        BallotListQueryRequestDto,
        GrpcBallotListQueryResponseResult
        > {

    private final BallotQueryProxy proxy;
    private final BallotQueryProcessorHelper helper;

    public BallotListQueryProcessor(
            BallotQueryProxy proxy,
            BallotQueryProcessorHelper helper
    ) {
        this.proxy = proxy;
        this.helper = helper;
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
                .map(this.helper::mapToBallotSchema)
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
