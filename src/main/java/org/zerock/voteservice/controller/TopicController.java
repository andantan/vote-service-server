package org.zerock.voteservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.dto.TopicDto;
import org.zerock.voteservice.grpc.GrpcTopicClient;
import org.zerock.voteservice.property.GrpcTopicConnectionProperties;

@RestController
@RequestMapping("/topic")
public class TopicController {
    private final GrpcTopicClient grpcTopicClient;

    public TopicController(GrpcTopicConnectionProperties grpcTopicConnectionProperties) {
        this.grpcTopicClient = new GrpcTopicClient(
                grpcTopicConnectionProperties.getHost(), grpcTopicConnectionProperties.getPort()
        );
    }

    @PostMapping("/new")
    public ResponseEntity<String> newTopic(@RequestBody TopicDto dto) {
        String info = grpcTopicClient.submitTopic(
                dto.getTopic(),
                dto.getDuration()
        );

        return ResponseEntity.ok(info);
    }
}
