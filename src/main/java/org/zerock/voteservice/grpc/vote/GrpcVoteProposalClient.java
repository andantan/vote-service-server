package org.zerock.voteservice.grpc.vote;

import domain.vote.proposal.protocol.BlockchainVoteProposalServiceGrpc;
import domain.vote.proposal.protocol.VoteProposalRequest;
import domain.vote.proposal.protocol.VoteProposalResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
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
        log.info("#[gRPC]#[To  : Blockchain-Node-Server] ProposalVote request: vote_id={}, duration='{}'",
                topic, duration
        );

        VoteProposalRequest request = VoteProposalRequest.newBuilder()
                .setTopic(topic)
                .setDuration(duration)
                .build();

        VoteProposalResponse response = stub.proposalVote(request);

        log.info("#[gRPC]#[From: Blockchain-Node-Server] ProposalVote response: Success={}, Message='{}', Status={}",
                String.valueOf(response.getSuccess()),
                response.getMessage(),
                response.getStatus()
        );

        Map<String, String> resp = new HashMap<>();

        resp.put("status", response.getStatus());
        resp.put("message", response.getMessage());
        resp.put("success", String.valueOf(response.getSuccess()));

        return resp;
    }
}
