package org.zerock.voteservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.property.GrpcVoteConnectionProperties;
import org.zerock.voteservice.grpc.GrpcVoteClient;
import org.zerock.voteservice.dto.VoteDto;

import java.util.Map;

@RestController
@RequestMapping("/vote")
public class VoteController {
    private final GrpcVoteClient grpcVoteClient;

    public VoteController(GrpcVoteConnectionProperties grpcVoteConnectionProps) {
        this.grpcVoteClient = new GrpcVoteClient(
                grpcVoteConnectionProps.getHost(), grpcVoteConnectionProps.getPort()
        );
    }

    @PostMapping("/submit")
    public Map<String,String> submitVote(@RequestBody VoteDto dto) {

        return grpcVoteClient.submitVote(
                dto.getHash(),
                dto.getOption(),
                dto.getTopic()
        );
    }
}
