package org.zerock.voteservice.adapter.in.web.controller.query;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.web.mapper.QueryApiEndpointMapper;
import org.zerock.voteservice.adapter.in.web.controller.query.docs.QueryBallotApiDoc;
import org.zerock.voteservice.adapter.in.web.controller.query.processor.BallotQueryProcessor;
import org.zerock.voteservice.adapter.in.web.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.dto.query.QueryBallotRequestDto;

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
        if (userHash == null || userHash.length() != 64) {
            return this.ballotQueryProcessor.getErrorResponse("INVALID_USER_HASH");
        }

        QueryBallotRequestDto dto = QueryBallotRequestDto.builder()
                .userHash(userHash)
                .build();

        GetUserBallotsResponse userBallots = this.ballotQueryProcessor.getUserBallots(dto);

        if (!userBallots.getQueried()) {
            return this.ballotQueryProcessor.getErrorResponse(userBallots.getStatus());
        }

        return this.ballotQueryProcessor.getSuccessResponse(userBallots.getStatus(), userBallots.getBallotsList());
    }
}
