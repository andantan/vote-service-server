package org.zerock.voteservice.adapter.in.web.controller.query.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import domain.event.ballot.query.protocol.Ballot;
import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.zerock.voteservice.adapter.out.grpc.mongodbServer.voteData.BallotQueryEventServiceGrpcStub;
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

    private final BallotQueryEventServiceGrpcStub ballotQueryEventServiceGrpcStub;

    public BallotQueryProcessor(
            BallotQueryEventServiceGrpcStub ballotQueryEventServiceGrpcStub
    ) {
        this.ballotQueryEventServiceGrpcStub = ballotQueryEventServiceGrpcStub;
    }

    public GetUserBallotsResponse getUserBallots(QueryBallotRequestDto dto) {
        return this.ballotQueryEventServiceGrpcStub.getUserBallots(dto.getUserHash());
    }

    public ResponseEntity<QueryBallotResponseDto> getSuccessResponse(String internalStatus, List<Ballot> userBallots) {
        List<BallotSchema> ballotSchemas = userBallots.stream()
                .map(this::mappingBallotSchema)
                .toList();

        String successMessage = "조회가 완료되었습니다.";

        QueryBallotResponseDto successDto = QueryBallotResponseDto.builder()
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
        QueryBallotErrorStatus errorStatus = QueryBallotErrorStatus.fromCode(internalStatus);
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
