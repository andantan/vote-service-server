package org.zerock.voteservice.adapter.in.blockchain.orchestrator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.blockchain.BlockCreatedEventBlockchainRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.grpc.BlockCreatedEventGrpcRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.processor.BlockCreatedEventProcessor;
import org.zerock.voteservice.adapter.in.common.ResponseDto;
import org.zerock.voteservice.adapter.in.common.extend.AbstractOrchestrator;
import org.zerock.voteservice.adapter.in.web.controller.helper.ControllerHelper;
import org.zerock.voteservice.adapter.out.grpc.result.GrpcBlockCreatedEventResponseResult;
import org.zerock.voteservice.security.user.UserAuthenticationDetails;

@Log4j2
@Component
public class BlockCreatedEventOrchestrator
        extends AbstractOrchestrator<BlockCreatedEventBlockchainRequestDto, ResponseDto> {

    private final BlockCreatedEventProcessor blockCreatedEventProcessor;

    public BlockCreatedEventOrchestrator(
            ControllerHelper controllerHelper,
            BlockCreatedEventProcessor blockCreatedEventProcessor
    ) {
        super(controllerHelper);
        this.blockCreatedEventProcessor = blockCreatedEventProcessor;
    }

    @Override
    protected ResponseEntity<? extends ResponseDto> executeBusinessLogic(
            BlockCreatedEventBlockchainRequestDto requestDto,
            UserAuthenticationDetails userDetails,
            String logPrefix
    ) {
        log.debug("{}Attempting report created block for block-height: {}", logPrefix, requestDto.getHeight());

        BlockCreatedEventGrpcRequestDto reportRequestDto = BlockCreatedEventGrpcRequestDto.builder()
                .topic(requestDto.getTopic())
                .txCount(requestDto.getTransactionCount())
                .height(requestDto.getHeight())
                .build();

        GrpcBlockCreatedEventResponseResult reportResult = processStep(
                blockCreatedEventProcessor, reportRequestDto, logPrefix, "Report Created Block"
        );

        if (!reportResult.getSuccess()) {
            return createFailureResponse(
                    blockCreatedEventProcessor, reportResult, logPrefix, "Report Created Block"
            );
        }

        return createSuccessResponse(
                blockCreatedEventProcessor, reportRequestDto, reportResult, logPrefix, "Report Created Block"
        );
    }
}
