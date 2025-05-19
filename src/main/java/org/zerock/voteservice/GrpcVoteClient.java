package org.zerock.voteservice;

import com.example.com.BlockchainServiceGrpc;
import com.example.com.voteMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcVoteClient {
    private final BlockchainServiceGrpc.BlockchainServiceBlockingStub stub;

    public GrpcVoteClient(String grpcHost, int grpcPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext() // TLS 사용 시 제거
                .build();

        stub = BlockchainServiceGrpc.newBlockingStub(channel);
    }

    public long submitVote(String voteHash, String voteOption, String electionId) {
        voteMessage.VoteRequest request = voteMessage.VoteRequest.newBuilder()
                .setVoteHash(voteHash)
                .setVoteOption(voteOption)
                .setElectionId(electionId)
                .build();

        voteMessage.VoteResponse response = stub.submitVote(request);
        return response.getBlockHeight();
    }
}
