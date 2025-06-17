package org.zerock.voteservice.controller.query;

import domain.event.ballot.query.protocol.GetUserBallotsResponse;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.controller.mapper.ApiQueryEndpointMapper;
import org.zerock.voteservice.controller.query.processor.BallotQueryProcessor;
import org.zerock.voteservice.dto.ResponseDto;
import org.zerock.voteservice.dto.query.BallotQueryRequestDto;

@Log4j2
@RestController
public class ApiQueryBallotController extends ApiQueryEndpointMapper {

    private final BallotQueryProcessor ballotQueryProcessor;

    public ApiQueryBallotController(BallotQueryProcessor ballotQueryProcessor) {
        this.ballotQueryProcessor = ballotQueryProcessor;
    }

    @GetMapping("/{userHash}/votes")
    public ResponseEntity<? extends ResponseDto> getUserVotes(
            @ModelAttribute final BallotQueryRequestDto dto
    ) {
        GetUserBallotsResponse userBallots = this.ballotQueryProcessor.getUserBallots(dto);

        if (!userBallots.getQueried()) {
            return this.ballotQueryProcessor.getErrorResponse(userBallots.getStatus());
        }

        return this.ballotQueryProcessor.getSuccessResponse(userBallots.getStatus(), userBallots.getBallotsList());
    }
}
