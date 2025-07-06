package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalDetailQueryRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.ProposalDetailQueryWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.ProposalDetailQueryProcessor;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalDetailQueryResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class ProposalDetailQueryOrchestrator extends AbstractOrchestrator<ProposalDetailQueryWebClientRequestDto, ResponseDto> {

    private final ProposalDetailQueryProcessor proposalDetailQueryProcessor;

    public ProposalDetailQueryOrchestrator(
            ControllerHelper controllerHelper,
            ProposalDetailQueryProcessor proposalDetailQueryProcessor
    ) {
        super(controllerHelper);
        this.proposalDetailQueryProcessor = proposalDetailQueryProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            ProposalDetailQueryWebClientRequestDto requestDto,
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

        log.debug("{}Attempting query ballot list for topic: {}", logPrefix, requestDto.getTopic());

        ProposalDetailQueryRequestDto queryRequestDto = ProposalDetailQueryRequestDto.builder()
                .topic(requestDto.getTopic())
                .build();

        GrpcProposalDetailQueryResponseResult queryResult = processStep(
                proposalDetailQueryProcessor, queryRequestDto, logPrefix, "Proposal Detail Query"
        );

        if (!queryResult.getSuccess()) {
            return createFailureResponse(
                    proposalDetailQueryProcessor, queryResult, logPrefix, "Proposal Detail Query"
            );
        }

        return createSuccessResponse(
                proposalDetailQueryProcessor, queryRequestDto, queryResult, logPrefix, "Proposal Detail Query"
        );
    }
}
