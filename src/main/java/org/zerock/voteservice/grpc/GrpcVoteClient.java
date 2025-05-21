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

    public long submitVote(String voteHash, String voteOption, String voteId) {
        voteMessage.VoteRequest request = voteMessage.VoteRequest.newBuilder()
                .setVoteHash(voteHash)
                .setVoteOption(voteOption)
                .setVoteId(voteId)
                .build();

        voteMessage.VoteResponse response = stub.submitVote(request);
        return response.getBlockHeight();
    }
}
