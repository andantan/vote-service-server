package org.zerock.voteservice.adapter.in.web.controller.query.ballot.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.ballot.query.protocol.Ballot;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.zerock.voteservice.adapter.common.ErrorResponseProcessor;
import org.zerock.voteservice.adapter.out.grpc.proxy.query.BallotQueryProxy;

import org.zerock.voteservice.adapter.in.web.dto.query.schema.BallotSchema;
import org.zerock.voteservice.adapter.in.web.dto.query.ballot.QueryBallotRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.ballot.QueryBallotResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.error.QueryErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.error.status.QueryBallotErrorStatus;

import org.zerock.voteservice.tool.time.DateConverter;

import java.time.LocalDateTime;

import java.util.List;

@Log4j2
@Service
public class BallotQueryProcessor implements ErrorResponseProcessor {

    private final BallotQueryProxy ballotQueryProxy;

    public BallotQueryProcessor(BallotQueryProxy ballotQueryProxy) {
        this.ballotQueryProxy = ballotQueryProxy;
    }

    public BallotQueryProcessorResult validateUserHash(String userHash) {
        if (userHash == null) {
            return BallotQueryProcessorResult.failure("DECODE_ERROR");
        }

        if (userHash.length() != 64) {
            return BallotQueryProcessorResult.failure("INVALID_HASH_LENGTH");
        }

        return BallotQueryProcessorResult.successWithoutData();
    }

    public BallotQueryProcessorResult processBallotQuery(QueryBallotRequestDto dto) {
        GetUserBallotsResponse userBallots = this.ballotQueryProxy.getUserBallots(dto);

        if (!userBallots.getQueried()) {
            return BallotQueryProcessorResult.failure(userBallots.getStatus());
        }

        return BallotQueryProcessorResult.success(userBallots.getStatus(), userBallots.getBallotsList());
    }

    public ResponseEntity<QueryBallotResponseDto> getSuccessResponse(QueryBallotRequestDto dto, BallotQueryProcessorResult result) {
        List<BallotSchema> ballotSchemas = result.getBallotList().stream()
                .map(this::mappingBallotSchema)
                .toList();

        QueryBallotResponseDto successDto = QueryBallotResponseDto.builder()
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

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(BallotQueryProcessorResult result) {
        QueryBallotErrorStatus errorStatus = QueryBallotErrorStatus.fromCode(result.getStatus());
        QueryErrorResponseDto errorDto = QueryErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(String status) {
        QueryBallotErrorStatus errorStatus = QueryBallotErrorStatus.fromCode(status);
        QueryErrorResponseDto errorDto = QueryErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }

    private BallotSchema mappingBallotSchema(Ballot ballot) {
        LocalDateTime kstSubmittedAt = null;

        try {
            kstSubmittedAt = DateConverter.toKstLocalDateTime(ballot.getSubmittedAt());
        } catch (NullPointerException ignorable) {
            log.warn("submitted_at field is missing for voteHash: {}", ballot.getVoteHash());
        }

        return BallotSchema.builder()
                .voteHash(ballot.getVoteHash())
                .topic(ballot.getTopic())
                .submittedAt(kstSubmittedAt)
                .build();
    }
}
