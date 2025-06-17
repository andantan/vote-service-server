package org.zerock.voteservice.controller.event.processor;

import domain.event.pending.protocol.ReportPendingEventResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.voteservice.dto.event.ExpiredPendingEventDto;
import org.zerock.voteservice.grpc.event.GrpcPendingEventClient;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class PendingProcessor {
    private final GrpcPendingEventClient grpcPendingEventClient;

    public PendingProcessor(GrpcPendingEventClient grpcPendingEventClient) {
        this.grpcPendingEventClient = grpcPendingEventClient;
    }

    public ReportPendingEventResponse reportExpiredPendingEvent(ExpiredPendingEventDto dto) {
        return this.grpcPendingEventClient.reportExpiredPendingEvent(dto.getVoteId(), dto.getVoteCount(), dto.getVoteOptions());
    }

    public Map<String, Object> getResponse(ExpiredPendingEventDto dto, ReportPendingEventResponse response) {
        Map<String, Object> result = new HashMap<>();

        result.put("cached", response.getCached());
        result.put("status", response.getStatus());
        result.put("vote_id", dto.getVoteId());
        result.put("vote_count", dto.getVoteCount());

        return result;
    }
}
