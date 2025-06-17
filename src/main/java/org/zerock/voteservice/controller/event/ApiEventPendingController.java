package org.zerock.voteservice.controller.event;

import domain.event.pending.protocol.ReportPendingEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.controller.event.processor.PendingProcessor;
import org.zerock.voteservice.controller.mapper.ApiEventEndpointMapper;
import org.zerock.voteservice.dto.event.ExpiredPendingEventDto;

import java.util.Map;

@Log4j2
@RestController
public class ApiEventPendingController extends ApiEventEndpointMapper {
    private final PendingProcessor pendingProcessor;

    public ApiEventPendingController(PendingProcessor pendingProcessor) {
        this.pendingProcessor = pendingProcessor;
    }


    @PostMapping("/expired-pending")
    public Map<String, Object> eventNewBlock(@RequestBody ExpiredPendingEventDto dto) {
        ReportPendingEventResponse response = this.pendingProcessor.reportExpiredPendingEvent(dto);

        return this.pendingProcessor.getResponse(dto, response);
    }
}
