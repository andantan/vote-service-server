package org.zerock.voteservice.controller.vote;

import org.springframework.web.bind.annotation.*;
import org.zerock.voteservice.property.vote.GrpcVoteSubmitConnectionProperties;
import org.zerock.voteservice.grpc.vote.GrpcVoteSubmitClient;
import org.zerock.voteservice.dto.vote.VoteSubmitDto;

import java.util.Map;

@RestController
public class VoteSubmitController extends VoteRequestMapper {
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
