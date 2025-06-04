package org.zerock.voteservice.controller.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.dto.event.BlockCreatedEventDto;
import org.zerock.voteservice.grpc.GrpcBlockEventClient;
import org.zerock.voteservice.property.GrpcBlockEventConnectionProperties;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class CreatedBlockEventController extends EventRequestMapper {
    private final GrpcBlockEventClient grpcBlockEventClient;

    public CreatedBlockEventController(GrpcBlockEventConnectionProperties grpcTopicConnectionProperties) {
        this.grpcBlockEventClient = new GrpcBlockEventClient(
                grpcTopicConnectionProperties.getHost(), grpcTopicConnectionProperties.getPort()
        );
    }

    @PostMapping("/new-block")
    public Map<String, Object> eventNewBlock(@RequestBody BlockCreatedEventDto dto) {
        log.info("New block creation event received: votingId='{}', height={}",
                dto.getVotingId(),
                dto.getHeight()
        );

        Map<String, Object> grpcResponse = grpcBlockEventClient.reportCreatedBlockEvent(
                dto.getVotingId(),
                dto.getHeight()
        );

        log.info("ReportCreatedBlockEvent Response: Success={}, Message='{}'",
                grpcResponse.get("success"),
                grpcResponse.get("message")
        );

        Map<String, Object> result = new HashMap<>();

        result.put("caching", true);
        result.put("voting_id", dto.getVotingId());
        result.put("height", dto.getHeight());

        return result;
    }
}
