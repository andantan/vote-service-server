package org.zerock.voteservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.property.GrpcVoteSubmitConnectionProperties;
import org.zerock.voteservice.grpc.GrpcVoteSubmitClient;
import org.zerock.voteservice.dto.VoteSubmitDto;

import java.util.Map;

@RestController
@RequestMapping("/vote")
public class VoteSubmitController {
    private final GrpcVoteSubmitClient grpcVoteClient;

    public VoteSubmitController(GrpcVoteSubmitConnectionProperties grpcVoteConnectionProps) {
        this.grpcVoteClient = new GrpcVoteSubmitClient(
                grpcVoteConnectionProps.getHost(), grpcVoteConnectionProps.getPort()
        );
    }

    @PostMapping("/submit")
    public Map<String,String> submitVote(@RequestBody VoteSubmitDto dto) {

        return grpcVoteClient.submitVote(
                dto.getHash(),
                dto.getOption(),
                dto.getTopic()
        );
    }
}
