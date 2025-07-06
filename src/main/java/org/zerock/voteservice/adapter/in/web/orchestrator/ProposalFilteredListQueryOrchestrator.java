package org.zerock.voteservice.adapter.in.web.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.grpc.ProposalFilteredListQueryGrpcRequestDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.request.client.ProposalFilteredListQueryWebClientRequestDto;
import org.zerock.voteservice.adapter.in.web.processor.ProposalFilteredListQueryProcessor;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcProposalFilteredListQueryResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class ProposalFilteredListQueryOrchestrator
        extends AbstractOrchestrator<ProposalFilteredListQueryWebClientRequestDto, ResponseDto> {

    private final ProposalFilteredListQueryProcessor proposalFilteredListQueryProcessor;

    public ProposalFilteredListQueryOrchestrator(
            ControllerHelper controllerHelper,
            ProposalFilteredListQueryProcessor proposalFilteredListQueryProcessor
    ) {
        super(controllerHelper);
        this.proposalFilteredListQueryProcessor = proposalFilteredListQueryProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            ProposalFilteredListQueryWebClientRequestDto requestDto,
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

        log.debug("{}Attempting query proposal filtered list: {} propsals", logPrefix, requestDto.getLimit());

        ProposalFilteredListQueryGrpcRequestDto queryRequestDto = ProposalFilteredListQueryGrpcRequestDto.builder()
                .summarize(requestDto.getSummarize())
                .expired(requestDto.getExpired())
                .sortOrder(requestDto.getSortOrder())
                .sortBy(requestDto.getSortBy())
                .skip(requestDto.getSkip())
                .limit(requestDto.getLimit())
                .build();

        GrpcProposalFilteredListQueryResponseResult queryResult = processStep(
                proposalFilteredListQueryProcessor, queryRequestDto, logPrefix, "Proposal Filtered List Query"
        );

        if (!queryResult.getSuccess()) {
            return createFailureResponse(
                    proposalFilteredListQueryProcessor, queryResult, logPrefix, "Ballot List Query"
            );
        }

        return createSuccessResponse(
                proposalFilteredListQueryProcessor, queryRequestDto, queryResult, logPrefix, "Ballot List Query"
        );
    }
}
