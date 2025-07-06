package org.zerock.voteservice.adapter.in.blockchain.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.blockchain.controller.mapper.UnicastNotificationEndpointMapper;
import org.zerock.voteservice.adapter.in.blockchain.domain.dto.requests.blockchain.PendingExpiredEventBlockchainRequestDto;
import org.zerock.voteservice.adapter.in.blockchain.orchestrator.PendingExpiredEventOrchestrator;
import org.zerock.voteservice.adapter.in.common.ResponseDto;


@Log4j2
@RestController
@RequiredArgsConstructor
public class PendingNotificationReceiverController extends UnicastNotificationEndpointMapper {

    private final PendingExpiredEventOrchestrator pendingExpiredEventOrchestrator;

    @PostMapping("/expired-pending")
    public ResponseEntity<? extends ResponseDto> receivePendingExpiredEvent(
            @RequestBody PendingExpiredEventBlockchainRequestDto dto
    ) {
        log.debug(">>>>>> Received /submit request. Delegating to PendingExpiredEventOrchestrator.");

        return pendingExpiredEventOrchestrator.orchestrate(dto);
    }
}
