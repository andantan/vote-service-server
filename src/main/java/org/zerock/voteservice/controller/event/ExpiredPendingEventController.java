package org.zerock.voteservice.controller.event;

import domain.event.pending.protocol.ReportPendingEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.controller.event.processor.PendingEventProcessor;
import org.zerock.voteservice.dto.event.ExpiredPendingEventDto;

import java.util.Map;

@Log4j2
@RestController
public class ExpiredPendingEventController extends EventRequestMapper {
    private final PendingEventProcessor pendingEventProcessor;

    public ExpiredPendingEventController(PendingEventProcessor pendingEventProcessor) {
        this.pendingEventProcessor = pendingEventProcessor;
    }


    @PostMapping("/expired-pending")
    public Map<String, Object> eventNewBlock(@RequestBody ExpiredPendingEventDto dto) {
        ReportPendingEventResponse response = this.pendingEventProcessor.reportExpiredPendingEvent(dto);

        return this.pendingEventProcessor.getResponse(dto, response);
    }
}
