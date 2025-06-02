package org.zerock.voteservice.grpc;

import com.example.com.BlockchainVoteProposalServiceGrpc;
import com.example.com.voteProposalMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcVoteProposalClient {
    private final BlockchainVoteProposalServiceGrpc.BlockchainVoteProposalServiceBlockingStub stub;

    public GrpcVoteProposalClient(String grpcTopicConnectionHost, int grpcTopicConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcTopicConnectionHost, grpcTopicConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteProposalServiceGrpc.newBlockingStub(channel);
    }

    public Map<String, String> proposalVote(String topic, int duration) {
        voteProposalMessage.VoteProposalRequest request = voteProposalMessage.VoteProposalRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        voteProposalMessage.VoteProposalResponse response = stub.proposalVote(request); 

        Map<String, String> resp = new HashMap<>();

        resp.put("status", response.getStatus());
        resp.put("message", response.getMessage());
        resp.put("success", String.valueOf(response.getSuccess()));

        return resp;
    }
}
