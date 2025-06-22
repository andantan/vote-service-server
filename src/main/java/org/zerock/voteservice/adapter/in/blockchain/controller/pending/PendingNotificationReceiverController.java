package org.zerock.voteservice.adapter.in.blockchain.controller.pending;

import domain.event.pending.protocol.PendingExpiredEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.adapter.in.blockchain.controller.pending.processor.PendingProcessor;
import org.zerock.voteservice.adapter.in.blockchain.controller.mapper.UnicastNotificationEndpointMapper;
import org.zerock.voteservice.adapter.in.blockchain.dto.pending.PendingExpiredEventDto;

import java.util.Map;

@Log4j2
@RestController
public class PendingNotificationReceiverController extends UnicastNotificationEndpointMapper {
    private final PendingProcessor pendingProcessor;

    public PendingNotificationReceiverController(PendingProcessor pendingProcessor) {
        this.pendingProcessor = pendingProcessor;
    }

    @PostMapping("/expired-pending")
    public Map<String, Object> receivePendingExpiredEvent(@RequestBody PendingExpiredEventDto dto) {
        PendingExpiredEventResponse response = this.pendingProcessor.reportPendingExpiredEvent(dto);

        return this.pendingProcessor.getResponse(dto, response);
    }
}
