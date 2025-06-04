package org.zerock.voteservice.controller.vote;

import org.springframework.web.bind.annotation.*;
import org.zerock.voteservice.dto.vote.VoteProposalDto;
import org.zerock.voteservice.grpc.GrpcVoteProposalClient;
import org.zerock.voteservice.property.GrpcVoteProposalConnectionProperties;

import java.util.Map;

@RestController
public class VoteProposalController extends VoteRequestMapper {
    private final GrpcVoteProposalClient grpcTopicClient;

    public VoteProposalController(GrpcVoteProposalConnectionProperties grpcTopicConnectionProperties) {
        this.grpcTopicClient = new GrpcVoteProposalClient(
                grpcTopicConnectionProperties.getHost(), grpcTopicConnectionProperties.getPort()
        );
    }


    @PostMapping("/proposal")
    public Map<String,String> proposalVote(@RequestBody VoteProposalDto dto) {

        return grpcTopicClient.proposalVote(
                dto.getTopic(),
                dto.getDuration()
        );
    }
}
