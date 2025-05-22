package org.zerock.voteservice.grpc;

import com.example.com.BlockchainVoteServiceGrpc;
import com.example.com.voteMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcVoteClient {
    private final BlockchainVoteServiceGrpc.BlockchainVoteServiceBlockingStub stub;

    public GrpcVoteClient(String grpcVoteConnectionHost, int grpcVoteConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcVoteConnectionHost, grpcVoteConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteServiceGrpc.newBlockingStub(channel);
    }

    public String submitVote(String hash, String option, String topic) {
        voteMessage.VoteRequest request = voteMessage.VoteRequest.newBuilder()
                .setHash(hash)
                .setOption(option)
                .setTopic(topic)
                .build();

        voteMessage.VoteResponse response = stub.submitVote(request);

        return String.format("status: %s, message: %s",
                response.getStatus(), response.getMessage());
    }
}
