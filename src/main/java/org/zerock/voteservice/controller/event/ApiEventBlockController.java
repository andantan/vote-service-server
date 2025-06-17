package org.zerock.voteservice.controller.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import domain.event.block.protocol.ReportBlockEventResponse;

import org.zerock.voteservice.controller.event.processor.BlockProcessor;
import org.zerock.voteservice.controller.mapper.ApiEventEndpointMapper;
import org.zerock.voteservice.dto.event.BlockCreatedEventDto;

import java.util.Map;

@Log4j2
@RestController
public class ApiEventBlockController extends ApiEventEndpointMapper {
    private final BlockProcessor blockProcessor;

    public ApiEventBlockController(BlockProcessor blockProcessor) {
        this.blockProcessor = blockProcessor;
    }

    @PostMapping("/new-block")
    public Map<String, Object> eventNewBlock(@RequestBody BlockCreatedEventDto dto) {
        ReportBlockEventResponse response = blockProcessor.reportCreatedBlockEvent(dto);

        return blockProcessor.getResponse(dto, response);
    }
}
