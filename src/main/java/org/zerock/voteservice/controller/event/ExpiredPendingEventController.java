package org.zerock.voteservice.controller.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.dto.event.ExpiredPendingEventDto;
import org.zerock.voteservice.grpc.event.GrpcPendingEventClient;
import org.zerock.voteservice.property.event.GrpcPendingEventConnectionProperties;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class ExpiredPendingEventController extends EventRequestMapper {
    private final GrpcPendingEventClient grpcPendingEventClient;

    public ExpiredPendingEventController(GrpcPendingEventConnectionProperties grpcPendingEventConnectionProperties) {
        this.grpcPendingEventClient = new GrpcPendingEventClient(
                grpcPendingEventConnectionProperties.getHost(), grpcPendingEventConnectionProperties.getPort()
        );
    }


    @PostMapping("/expired-pending")
    public Map<String, Object> eventNewBlock(@RequestBody ExpiredPendingEventDto dto) {
        log.info("[REST-Blockchain-Node-Server] Pending event received: vote_id='{}', vote_count={}, vote_option_counts={}",
                dto.getVoteId(),
                dto.getVoteCount(),
                dto.getVoteOptionCounts()
        );

        log.info("[gRPC-MongoDB-Cache-Server] ReportExpiredPendingEvent request: topic='{}', count={}, options={}",
                dto.getVoteId(),
                dto.getVoteCount(),
                dto.getVoteOptionCounts()
        );

        Map<String, Object> grpcResponse = grpcPendingEventClient.reportExpiredPendingEvent(
                dto.getVoteId(),
                dto.getVoteCount(),
                dto.getVoteOptionCounts()
        );

        log.info("[gRPC-MongoDB-Cache-Server] ReportExpiredPendingEvent Response: Success={}, Message='{}'",
                grpcResponse.get("success"),
                grpcResponse.get("message")
        );

//        if (dto.getVoteOptionCounts() != null && !dto.getVoteOptionCounts().isEmpty()) {
//            log.info("  Submits Option Details:");
//            dto.getVoteOptionCounts().forEach((key, value) ->
//                    log.info("    Option '{}': {} submissions", key, value));
//        }

        Map<String, Object> result = new HashMap<>();

        result.put("caching", grpcResponse.get("success"));
        result.put("message", grpcResponse.get("message"));
        result.put("vote_id", dto.getVoteId());
        result.put("vote_count", dto.getVoteCount());

        return result;
    }
}
