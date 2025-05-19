package org.zerock.voteservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
public class VoteController {
    private final GrpcVoteClient grpcClient;

    public VoteController() {
        this.grpcClient = new GrpcVoteClient("localhost", 9000);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitVote(@RequestBody VoteDto dto) {
        long height = grpcClient.submitVote(dto.getVoteHash(), dto.getVoteOption(), dto.getElectionId());
        return ResponseEntity.ok("gRPC 응답: " + height);
    }
}
