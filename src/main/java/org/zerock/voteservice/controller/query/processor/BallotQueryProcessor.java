package org.zerock.voteservice.controller.query.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.ballot.query.protocol.Ballot;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.zerock.voteservice.grpc.event.GrpcBallotQueryEventClient;
import org.zerock.voteservice.dto.query.schema.BallotSchema;
import org.zerock.voteservice.dto.query.BallotQueryRequestDto;
import org.zerock.voteservice.dto.query.BallotQueryResponseDto;
import org.zerock.voteservice.dto.query.QueryErrorResponseDto;
import org.zerock.voteservice.dto.query.error.BallotQueryErrorStatus;

import org.zerock.voteservice.tool.date.Converter;

import java.time.LocalDateTime;

import java.util.List;

@Log4j2
@Service
public class BallotQueryProcessor {

    private final GrpcBallotQueryEventClient grpcEventClient;

    public BallotQueryProcessor(
            GrpcBallotQueryEventClient grpcEventClient
    ) {
        this.grpcEventClient = grpcEventClient;
    }

    public GetUserBallotsResponse getUserBallots(BallotQueryRequestDto dto) {
        return this.grpcEventClient.getUserBallots(dto.getUserHash());
    }

    public ResponseEntity<BallotQueryResponseDto> getSuccessResponse(String internalStatus, List<Ballot> userBallots) {
        List<BallotSchema> ballotSchemas = userBallots.stream()
                .map(this::mappingBallotSchema)
                .toList();

        String successMessage = "조회가 완료되었습니다.";

        BallotQueryResponseDto successDto = BallotQueryResponseDto.builder()
                .success(true)
                .message(successMessage)
                .status(internalStatus)
                .httpStatusCode(HttpStatus.OK.value())
                .ballots(ballotSchemas)
                .ballotLength(ballotSchemas.size())
                .build();

        return new ResponseEntity<>(successDto, HttpStatus.valueOf(successDto.getHttpStatusCode()));
    }

    public ResponseEntity<QueryErrorResponseDto> getErrorResponse(String internalStatus) {
        BallotQueryErrorStatus errorStatus = BallotQueryErrorStatus.fromCode(internalStatus);
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
