package org.zerock.voteservice.adapter.in.blockchain.controller.pending.processor;

import domain.event.pending.protocol.PendingExpiredEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.adapter.in.blockchain.dto.pending.PendingExpiredEventDto;
import org.zerock.voteservice.adapter.out.grpc.mongodbServer.blockchainData.PendingEventServiceGrpcStub;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class PendingProcessor {
    private final PendingEventServiceGrpcStub pendingEventServiceGrpcStub;

    public PendingProcessor(PendingEventServiceGrpcStub pendingEventServiceGrpcStub) {
        this.pendingEventServiceGrpcStub = pendingEventServiceGrpcStub;
    }

    public PendingExpiredEventResponse reportPendingExpiredEvent(PendingExpiredEventDto dto) {
        return this.pendingEventServiceGrpcStub.reportPendingExpiredEvent(dto.getVoteId(), dto.getVoteCount(), dto.getVoteOptions());
    }

    public Map<String, Object> getResponse(PendingExpiredEventDto dto, PendingExpiredEventResponse response) {
        Map<String, Object> result = new HashMap<>();

        result.put("cached", response.getCached());
        result.put("status", response.getStatus());
        result.put("vote_id", dto.getVoteId());
        result.put("vote_count", dto.getVoteCount());

        return result;
    }
}
