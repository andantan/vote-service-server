package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.BallotListQueryGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.BallotListQueryWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.BallotListQueryProcessor;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBallotListQueryResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class BallotListQueryOrchestrator extends AbstractOrchestrator<BallotListQueryWebClientRequestDto, ResponseDto> {

    private final BallotListQueryProcessor ballotListQueryProcessor;

    public BallotListQueryOrchestrator(
            ControllerHelper controllerHelper,
            BallotListQueryProcessor ballotListQueryProcessor
    ) {
        super(controllerHelper);
        this.ballotListQueryProcessor = ballotListQueryProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            BallotListQueryWebClientRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        if (userDetails == null) {
            return controllerHelper.failureHook(
                    logPrefix,
                    HttpStatus.UNAUTHORIZED,
                    "UNAUTHORIZED",
                    "인증 자격 증명이 부족합니다."
            );
        }

        log.debug("{}Attempting query ballot list for userHash: {}", logPrefix, requestDto.getUserHash());

        BallotListQueryGrpcRequestDto queryRequestDto = BallotListQueryGrpcRequestDto.builder()
                .userHash(requestDto.getUserHash())
                .build();

        GrpcBallotListQueryResponseResult queryResult = processStep(
                ballotListQueryProcessor, queryRequestDto, logPrefix, "Ballot List Query"
        );

        if (!queryResult.getSuccess()) {
            return createFailureResponse(
                    ballotListQueryProcessor, queryResult, logPrefix, "Ballot List Query"
            );
        }

        return createSuccessResponse(
                ballotListQueryProcessor, queryRequestDto, queryResult, logPrefix, "Ballot List Query"
        );
    }
}
