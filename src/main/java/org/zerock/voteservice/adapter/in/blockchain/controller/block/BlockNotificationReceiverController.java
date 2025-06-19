package org.zerock.voteservice.adapter.in.blockchain.controller.block;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import domain.event.block.protocol.BlockCreatedEventResponse;

import org.zerock.voteservice.adapter.in.blockchain.controller.block.processor.BlockProcessor;
import org.zerock.voteservice.adapter.in.blockchain.mapper.UnicastNotificationEndpointMapper;
import org.zerock.voteservice.adapter.in.blockchain.dto.block.BlockCreatedEventDto;

import java.util.Map;

@Log4j2
@RestController
public class BlockNotificationReceiverController extends UnicastNotificationEndpointMapper {
    private final BlockProcessor blockProcessor;

    public BlockNotificationReceiverController(BlockProcessor blockProcessor) {
        this.blockProcessor = blockProcessor;
    }

    @PostMapping("/new-block")
    public Map<String, Object> receiveBlockCreatedEvent(@RequestBody BlockCreatedEventDto dto) {
        BlockCreatedEventResponse response = blockProcessor.reportBlockCreatedEvent(dto);

        return blockProcessor.getResponse(dto, response);
    }
}
