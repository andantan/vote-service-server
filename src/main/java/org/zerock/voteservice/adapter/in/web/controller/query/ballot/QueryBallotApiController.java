package org.zerock.voteservice.adapter.in.web.controller.query.ballot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.web.controller.query.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.query.ballot.docs.QueryBallotApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.query.ballot.processor.BallotQueryResult;
import org.zerock.voteservice.adapter.in.web.controller.query.ballot.processor.BallotQueryProcessor;

import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.ballot.QueryBallotRequestDto;

@RestController
public class QueryBallotApiController extends QueryApiEndpointMapper {

    private final BallotQueryProcessor ballotQueryProcessor;

    public QueryBallotApiController(BallotQueryProcessor ballotQueryProcessor) {
        this.ballotQueryProcessor = ballotQueryProcessor;
    }

    @QueryBallotApiDoc
    @GetMapping("/{userHash}/votes")
    public ResponseEntity<? extends ResponseDto> getUserVotes(
            @PathVariable(value = "userHash") final String userHash
    ) {
        BallotQueryResult hashValidationResult = this.ballotQueryProcessor.validateUserHash(userHash);

        if (!hashValidationResult.getSuccess()) {
            return this.ballotQueryProcessor.getErrorResponse(hashValidationResult);
        }

        QueryBallotRequestDto dto = QueryBallotRequestDto.builder()
                .userHash(userHash)
                .build();

        BallotQueryResult result = this.ballotQueryProcessor.processBallotQuery(dto);

        if (!result.getSuccess()) {
            return this.ballotQueryProcessor.getErrorResponse(result);
        }

        return this.ballotQueryProcessor.getSuccessResponse(dto, result);
    }
}
