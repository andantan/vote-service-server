package org.zerock.voteservice.controller.query.processor;

import com.google.protobuf.Timestamp;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.ballot.query.protocol.Ballot;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.zerock.voteservice.dto.query.schema.BallotSchema;
import org.zerock.voteservice.dto.query.BallotQueryRequestDto;
import org.zerock.voteservice.dto.query.BallotQueryResponseDto;
import org.zerock.voteservice.dto.query.QueryErrorResponseDto;
import org.zerock.voteservice.dto.query.error.BallotQueryErrorStatus;
import org.zerock.voteservice.grpc.event.GrpcBallotQueryEventClient;

import java.time.ZoneId;
import java.time.Instant;
import java.time.LocalDateTime;

import java.util.List;

@Log4j2
@Service
public class BallotQueryProcessor {

    private final GrpcBallotQueryEventClient ballotQueryEventClient;

    public BallotQueryProcessor(
            GrpcBallotQueryEventClient ballotQueryEventClient
    ) {
        this.ballotQueryEventClient = ballotQueryEventClient;
    }

    public GetUserBallotsResponse getUserBallots(BallotQueryRequestDto dto) {
        return this.ballotQueryEventClient.getUserBallots(dto.getUserHash());
    }

    public ResponseEntity<BallotQueryResponseDto> getSuccessResponse(String internalStatus, List<Ballot> userBallots) {
        List<BallotSchema> ballotSchemas = userBallots.stream()
                .map(this::mappingSchema)
                .toList();

        String successMessage = String.format("조회가 완료되었습니다. (총 투표지 개수: %d)", ballotSchemas.size());

        BallotQueryResponseDto successDto = BallotQueryResponseDto.builder()
                .success(true)
                .message(successMessage)
                .status(internalStatus)
                .httpStatusCode(HttpStatus.OK.value())
                .ballots(ballotSchemas)
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(String internalStatus) {
        BallotQueryErrorStatus errorStatus = BallotQueryErrorStatus.valueOf(internalStatus);
        QueryErrorResponseDto errorDto = QueryErrorResponseDto.from(errorStatus);

        return new ResponseEntity<>(errorDto, HttpStatus.valueOf(errorDto.getHttpStatusCode()));
    }

    private BallotSchema mappingSchema(Ballot ballot) {
        LocalDateTime kstSubmittedAt = null;

        if (ballot.hasSubmittedAt()) {
            Timestamp submittedTimestamp = ballot.getSubmittedAt();

            Instant instant = Instant.ofEpochSecond(submittedTimestamp.getSeconds(), submittedTimestamp.getNanos());

            kstSubmittedAt = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
        } else {
            log.warn("submitted_at field is missing for voteHash: {}", ballot.getVoteHash());
        }

        return BallotSchema.builder()
                .voteHash(ballot.getVoteHash())
                .topic(ballot.getTopic())
                .submittedAt(kstSubmittedAt)
                .build();
    }
}
