package org.zerock.voteservice.controller.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import domain.event.block.protocol.ReportBlockEventResponse;

import org.zerock.voteservice.controller.event.processor.BlockEventProcessor;
import org.zerock.voteservice.dto.event.BlockCreatedEventDto;

import java.util.Map;

@Log4j2
@RestController
public class CreatedBlockEventController extends EventRequestMapper {
    private final BlockEventProcessor blockEventProcessor;

    public CreatedBlockEventController(BlockEventProcessor blockEventProcessor) {
        this.blockEventProcessor = blockEventProcessor;
    }

    @PostMapping("/new-block")
    public Map<String, Object> eventNewBlock(@RequestBody BlockCreatedEventDto dto) {
        ReportBlockEventResponse response = blockEventProcessor.reportCreatedBlockEvent(dto);

        return blockEventProcessor.getResponse(dto, response);
    }
}
