package org.zerock.voteservice.adapter.in.web.controller.query.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.ballot.query.protocol.Ballot;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.zerock.voteservice.adapter.out.grpc.proxy.query.BallotQueryProxy;

import org.zerock.voteservice.adapter.in.web.dto.query.schema.BallotSchema;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryBallotRequestDto;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryBallotResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryErrorResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.error.QueryBallotErrorStatus;

import org.zerock.voteservice.tool.date.Converter;

import java.time.LocalDateTime;

import java.util.List;

@Log4j2
@Service
public class BallotQueryProcessor {

    private final BallotQueryProxy ballotQueryProxy;

    public BallotQueryProcessor(BallotQueryProxy ballotQueryProxy) {
        this.ballotQueryProxy = ballotQueryProxy;
    }

    public BallotQueryResult validateUserHash(String userHash) {
        if (userHash == null) {
            return BallotQueryResult.failure("DECODE_ERROR");
        }

        if (userHash.length() != 64) {
            return BallotQueryResult.failure("INVALID_HASH_LENGTH");
        }

        return BallotQueryResult.successWithoutData();
    }

    public BallotQueryResult processBallotQuery(QueryBallotRequestDto dto) {
        GetUserBallotsResponse userBallots = this.ballotQueryProxy.getUserBallots(dto);

        if (!userBallots.getQueried()) {
            return BallotQueryResult.failure(userBallots.getStatus());
        }

        return BallotQueryResult.success(userBallots.getStatus(), userBallots.getBallotsList());
    }

    public ResponseEntity<QueryBallotResponseDto> getSuccessResponse(QueryBallotRequestDto dto, BallotQueryResult result) {
        List<BallotSchema> ballotSchemas = result.getBallots().stream()
                .map(this::mappingBallotSchema)
                .toList();

        QueryBallotResponseDto successDto = QueryBallotResponseDto.builder()
                .success(result.getSuccess())
                .message(result.getMessage())
                .status(result.getStatus())
                .httpStatusCode(result.getHttpStatusCode())
                .userHash(dto.getUserHash())
                .ballots(ballotSchemas)
                .ballotLength(ballotSchemas.size())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(BallotQueryResult result) {
        QueryBallotErrorStatus errorStatus = QueryBallotErrorStatus.fromCode(result.getStatus());
        QueryErrorResponseDto errorDto = QueryErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }

    private BallotSchema mappingBallotSchema(Ballot ballot) {
        LocalDateTime kstSubmittedAt = null;

        try {
            kstSubmittedAt = Converter.toKstLocalDateTime(ballot.getSubmittedAt());
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
