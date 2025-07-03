package org.zerock.voteservice.adapter.in.blockchain.controller.block.processor;

import domain.event.block.protocol.BlockCreatedEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.blockchain.dto.block.BlockCreatedEventDto;
import org.zerock.voteservice.adapter.out.grpc.stub.BlockEventServiceGrpcStub;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class BlockProcessor {
    private final BlockEventServiceGrpcStub blockEventServiceGrpcStub;

    public BlockProcessor(BlockEventServiceGrpcStub blockEventServiceGrpcStub) {
        this.blockEventServiceGrpcStub = blockEventServiceGrpcStub;
    }

    public BlockCreatedEventResponse reportBlockCreatedEvent(BlockCreatedEventDto dto) {
        return this.blockEventServiceGrpcStub.reportBlockCreatedEvent(
                dto.getTopic(), dto.getTransactionCount(), dto.getHeight()
        );
    }

    public Map<String, Object> getResponse(BlockCreatedEventDto dto, BlockCreatedEventResponse response) {
        Map<String, Object> result = new HashMap<>();

        result.put("cached", response.getCached());
        result.put("status", response.getStatus());
        result.put("topic", dto.getTopic());
        result.put("transaction_count", dto.getTransactionCount());
        result.put("height", dto.getHeight());

        return result;
    }
}
