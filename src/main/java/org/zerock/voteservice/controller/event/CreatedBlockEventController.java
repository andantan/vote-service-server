package org.zerock.voteservice.controller.event;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.dto.event.BlockCreatedEventDto;
import org.zerock.voteservice.grpc.event.GrpcBlockEventClient;
import org.zerock.voteservice.property.event.GrpcBlockEventConnectionProperties;

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
        log.info("===================================================================================================");

        log.info("#[REST]#[From: Blockchain-Node-Server] Block event received: vote_id='{}', length={}, height={}",
                dto.getVoteId(),
                dto.getLength(),
                dto.getHeight()
        );

        Map<String, Object> grpcResponse = grpcBlockEventClient.reportCreatedBlockEvent(
                dto.getVoteId(),
                dto.getLength(),
                dto.getHeight()
        );

        Map<String, Object> result = new HashMap<>();

        result.put("caching", grpcResponse.get("success"));
        result.put("message", grpcResponse.get("message"));
        result.put("vote_id", dto.getVoteId());
        result.put("height", dto.getHeight());

        log.info("#[REST]#[To  : Blockchain-Node-Server] Block event response: vote_id='{}', height={}, caching={}, Message='{}'",
                result.get("vote_id"),
                result.get("height"),
                result.get("caching"),
                result.get("message")
        );
        log.info("===================================================================================================");

        return result;
    }
}
