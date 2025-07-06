package org.zerock.voteservice.adapter.in.blockchain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.zerock.voteservice.adapter.in.blockchain.controller.mapper.UnicastNotificationEndpointMapper;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.blockchain.BlockCreatedEventBlockchainRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.orchestrator.BlockCreatedEventOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;


@Log4j2
@RestController
@RequiredArgsConstructor
public class BlockNotificationReceiverController extends UnicastNotificationEndpointMapper {

    private final BlockCreatedEventOrchestrator blockCreatedEventOrchestrator;

    @PostMapping("/new-block")
    public ResponseEntity<? extends ResponseDto> receiveBlockCreatedEvent(
            @RequestBody BlockCreatedEventBlockchainRequestDto dto
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to BlockCreatedEventOrchestrator.");

        return blockCreatedEventOrchestrator.orchestrate(dto);
    }
}
