package org.zerock.voteservice.controller.event.processor;

import domain.event.block.protocol.ReportBlockEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.dto.event.BlockCreatedEventDto;
import org.zerock.voteservice.grpc.event.GrpcBlockEventClient;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class BlockProcessor {
    private final GrpcBlockEventClient grpcBlockEventClient;

    public BlockProcessor(GrpcBlockEventClient grpcBlockEventClient) {
        this.grpcBlockEventClient = grpcBlockEventClient;
    }

    public ReportBlockEventResponse reportCreatedBlockEvent(BlockCreatedEventDto dto) {
        return this.grpcBlockEventClient.reportCreatedBlockEvent(dto.getVoteId(), dto.getLength(), dto.getHeight());
    }

    public Map<String, Object> getResponse(BlockCreatedEventDto dto, ReportBlockEventResponse response) {
        Map<String, Object> result = new HashMap<>();

        result.put("cached", response.getCached());
        result.put("status", response.getStatus());
        result.put("vote_id", dto.getVoteId());
        result.put("length", dto.getLength());
        result.put("height", dto.getHeight());

        return result;
    }
}
