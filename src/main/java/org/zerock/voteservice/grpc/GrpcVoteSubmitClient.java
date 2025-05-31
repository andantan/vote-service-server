package org.zerock.voteservice.grpc;

import com.example.com.BlockchainVoteSubmitServiceGrpc;
import com.example.com.voteSubmitMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcVoteClient {
    private final BlockchainVoteSubmitServiceGrpc.BlockchainVoteSubmitServiceBlockingStub stub;

    public GrpcVoteClient(String grpcVoteConnectionHost, int grpcVoteConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcVoteConnectionHost, grpcVoteConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteSubmitServiceGrpc.newBlockingStub(channel);
    }

    public Map<String, String> submitVote(String hash, String option, String topic) {
        voteSubmitMessage.VoteSubmitRequest request = voteSubmitMessage.VoteSubmitRequest.newBuilder()
                .setHash(hash)
                .setOption(option)
                .setTopic(topic)
                .build();

        voteSubmitMessage.VoteSubmitResponse response = stub.submitVote(request);

        Map<String, String> resp = new HashMap<>();

        resp.put("status", response.getStatus());
        resp.put("message", response.getMessage());
        resp.put("success", String.valueOf(response.getSuccess()));

        return resp;
    }
}
