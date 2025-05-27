package org.zerock.voteservice.grpc;

import com.example.com.BlockchainVoteServiceGrpc;
import com.example.com.voteMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcVoteClient {
    private final BlockchainVoteServiceGrpc.BlockchainVoteServiceBlockingStub stub;

    public GrpcVoteClient(String grpcVoteConnectionHost, int grpcVoteConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcVoteConnectionHost, grpcVoteConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteServiceGrpc.newBlockingStub(channel);
    }

    public Map<String, String> submitVote(String hash, String option, String topic) {
        voteMessage.VoteRequest request = voteMessage.VoteRequest.newBuilder()
                .setHash(hash)
                .setOption(option)
                .setTopic(topic)
                .build();

        voteMessage.VoteResponse response = stub.submitVote(request);

        Map<String, String> resp = new HashMap<>();

        resp.put("status", response.getStatus());
        resp.put("message", response.getMessage());
        resp.put("success", String.valueOf(response.getSuccess()));

        return resp;
    }
}
