package org.zerock.voteservice.grpc.vote;

import domain.vote.submit.protocol.BlockchainVoteSubmitServiceGrpc;
import domain.vote.submit.protocol.VoteSubmitRequest;
import domain.vote.submit.protocol.VoteSubmitResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class GrpcVoteSubmitClient {
    private final BlockchainVoteSubmitServiceGrpc.BlockchainVoteSubmitServiceBlockingStub stub;

    public GrpcVoteSubmitClient(String grpcVoteConnectionHost, int grpcVoteConnectionPort) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcVoteConnectionHost, grpcVoteConnectionPort)
                .usePlaintext()
                .build();

        stub = BlockchainVoteSubmitServiceGrpc.newBlockingStub(channel);
    }

    public Map<String, String> submitVote(String hash, String option, String topic) {
        log.info("#[gRPC]#[To  : Blockchain-Node-Server] SubmitVote request:  topic='{}', hash={}, option={}",
                topic, hash, option
        );

        VoteSubmitRequest request = VoteSubmitRequest.newBuilder()
                .setHash(hash)
                .setOption(option)
                .setTopic(topic)
                .build();

        VoteSubmitResponse response = stub.submitVote(request);

        log.info("#[gRPC]#[From: Blockchain-Node-Server] SubmitVote response: Success={}, Message='{}', Status={}",
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
