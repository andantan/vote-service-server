package org.zerock.voteservice.controller.query;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.controller.mapper.ApiQueryEndpointMapper;
import org.zerock.voteservice.controller.query.docs.QueryBallotApiDoc;
import org.zerock.voteservice.controller.query.processor.BallotQueryProcessor;
import org.zerock.voteservice.dto.ResponseDto;
import org.zerock.voteservice.dto.query.BallotQueryRequestDto;

@RestController
public class ApiQueryBallotController extends ApiQueryEndpointMapper {

    private final BallotQueryProcessor ballotQueryProcessor;

    public ApiQueryBallotController(BallotQueryProcessor ballotQueryProcessor) {
        this.ballotQueryProcessor = ballotQueryProcessor;
    }

    @QueryBallotApiDoc
    @GetMapping("/{userHash}/votes")
    public ResponseEntity<? extends ResponseDto> getUserVotes(
            @PathVariable("userHash") final String userHash
    ) {
        if (userHash == null || userHash.length() != 64) {
            return this.ballotQueryProcessor.getErrorResponse("INVALID_USER_HASH");
        }

        BallotQueryRequestDto dto = BallotQueryRequestDto.builder()
                .userHash(userHash)
                .build();

        GetUserBallotsResponse userBallots = this.ballotQueryProcessor.getUserBallots(dto);

        if (!userBallots.getQueried()) {
            return this.ballotQueryProcessor.getErrorResponse(userBallots.getStatus());
        }

        return this.ballotQueryProcessor.getSuccessResponse(userBallots.getStatus(), userBallots.getBallotsList());
    }
}
