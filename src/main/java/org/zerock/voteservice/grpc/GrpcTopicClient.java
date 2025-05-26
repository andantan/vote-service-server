package org.zerock.voteservice.grpc;

import com.example.com.BlockchainTopicServiceGrpc;
import com.example.com.topicMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcTopicClient {
    private final BlockchainTopicServiceGrpc.BlockchainTopicServiceBlockingStub stub;

    public GrpcTopicClient(String grpcTopicConnectionHost, int grpcTopicConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcTopicConnectionHost, grpcTopicConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainTopicServiceGrpc.newBlockingStub(channel);
    }

    public Map<String, String> submitTopic(String topic, int duration) {
        topicMessage.TopicRequest request = topicMessage.TopicRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        topicMessage.TopicResponse response = stub.submitTopic(request);

        Map<String, String> resp = new HashMap<>();

        resp.put("status", response.getStatus());
        resp.put("message", response.getMessage());
        resp.put("success", String.valueOf(response.getSuccess()));

        return resp;
    }
}
