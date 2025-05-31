package org.zerock.voteservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.dto.TopicDto;
import org.zerock.voteservice.grpc.GrpcTopicClient;
import org.zerock.voteservice.property.GrpcTopicConnectionProperties;

import java.util.Map;

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
    public Map<String,String> newTopic(@RequestBody TopicDto dto) {

        return grpcTopicClient.submitTopic(
                dto.getTopic(),
                dto.getDuration()
        );
    }
}
