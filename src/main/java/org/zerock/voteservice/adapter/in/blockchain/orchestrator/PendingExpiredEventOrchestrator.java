package org.zerock.voteservice.adapter.in.blockchain.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.blockchain.PendingExpiredEventBlockchainRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc.PendingExpiredEventGrpcRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.processor.PendingExpiredEventProcessor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcPendingExpiredEventResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class PendingExpiredEventOrchestrator
        extends AbstractOrchestrator<PendingExpiredEventBlockchainRequestDto, ResponseDto> {

    private final PendingExpiredEventProcessor pendingExpiredEventProcessor;

    public PendingExpiredEventOrchestrator(
            ControllerHelper controllerHelper,
            PendingExpiredEventProcessor pendingExpiredEventProcessor
    ) {
        super(controllerHelper);
        this.pendingExpiredEventProcessor = pendingExpiredEventProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            PendingExpiredEventBlockchainRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting report expired pending for topic: {}", logPrefix, requestDto.getVoteId());

        PendingExpiredEventGrpcRequestDto requestReportDto = PendingExpiredEventGrpcRequestDto.builder()
                .voteId(requestDto.getVoteId())
                .voteCount(requestDto.getVoteCount())
                .voteOptions(requestDto.getVoteOptions())
                .build();

        GrpcPendingExpiredEventResponseResult reportResult = processStep(
                pendingExpiredEventProcessor, requestReportDto, logPrefix, "Report Expired Pending"
        );

        if (!reportResult.getSuccess()) {
            return createFailureResponse(
                    pendingExpiredEventProcessor, reportResult, logPrefix, "Report Expired Pending"
            );
        }

        return createSuccessResponse(
                pendingExpiredEventProcessor, requestReportDto, reportResult, logPrefix, "Report Expired Pending"
        );
    }
}
