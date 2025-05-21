package org.zerock.voteservice.grpc;

import com.example.com.BlockchainTopicServiceGrpc;
import com.example.com.topicMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcTopicClient {
    private final BlockchainTopicServiceGrpc.BlockchainTopicServiceBlockingStub stub;

    public GrpcTopicClient(String grpcTopicConnectionHost, int grpcTopicConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcTopicConnectionHost, grpcTopicConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainTopicServiceGrpc.newBlockingStub(channel);
    }

    public String submitTopic(String topicId, int topicDuration) {
        topicMessage.TopicRequest request = topicMessage.TopicRequest.newBuilder()
                .setTopicId(topicId)
                .setTopicDuration(topicDuration)
                .build();

        topicMessage.TopicResponse response = stub.submitTopic(request);

        return String.format("TopicId: %s, TopicDuration: %d",
                response.getTopicId(), response.getTopicDuration());
    }
}
