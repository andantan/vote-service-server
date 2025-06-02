package org.zerock.voteservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.voteservice.dto.VoteProposalDto;
import org.zerock.voteservice.grpc.GrpcVoteProposalClient;
import org.zerock.voteservice.property.GrpcVoteProposalConnectionProperties;

import java.util.Map;

@RestController
@RequestMapping("/topic")
public class VoteProposalController {
    private final GrpcVoteProposalClient grpcTopicClient;

    public VoteProposalController(GrpcVoteProposalConnectionProperties grpcTopicConnectionProperties) {
        this.grpcTopicClient = new GrpcVoteProposalClient(
                grpcTopicConnectionProperties.getHost(), grpcTopicConnectionProperties.getPort()
        );
    }


    @PostMapping("/new")
    public Map<String,String> proposalVote(@RequestBody VoteProposalDto dto) {

        return grpcTopicClient.proposalVote(
                dto.getTopic(),
                dto.getDuration()
        );
    }
}
