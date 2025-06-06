package org.zerock.voteservice.controller.vote;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.voteservice.dto.vote.VoteProposalDto;
import org.zerock.voteservice.grpc.event.GrpcProposalEventClient;
import org.zerock.voteservice.grpc.vote.GrpcVoteProposalClient;
import org.zerock.voteservice.property.event.GrpcProposalEventConnectionProperties;
import org.zerock.voteservice.property.vote.GrpcVoteProposalConnectionProperties;

import java.util.Map;

@Log4j2
@RestController
public class VoteProposalController extends VoteRequestMapper {
    private final GrpcVoteProposalClient grpcVoteProposalClient;
    private final GrpcProposalEventClient grpcProposalEventClient;

    public VoteProposalController(
            GrpcVoteProposalConnectionProperties grpcTopicConnectionProperties,
            GrpcProposalEventConnectionProperties grpcProposalEventConnectionProperties
    ) {
        this.grpcVoteProposalClient = new GrpcVoteProposalClient(
                grpcTopicConnectionProperties.getHost(), grpcTopicConnectionProperties.getPort()
        );
        this.grpcProposalEventClient = new GrpcProposalEventClient(
                grpcProposalEventConnectionProperties.getHost(), grpcProposalEventConnectionProperties.getPort()
        );
    }

    @PostMapping("/proposal")
    public Map<String, String> proposalVote(@RequestBody VoteProposalDto dto) {
        log.info("===================================================================================================");
        log.info("#[REST]#[From: WEB-Client] Proposal-vote request received: topic='{}', duration={}",
                dto.getTopic(),
                dto.getDuration()
        );

        Map<String, Object> grpcProposalValidationResponse = grpcProposalEventClient.validateNewProposalEvent(
                dto.getTopic(),
                dto.getDuration()
        );

        Map<String, String> grpcProposalVoteResponse = grpcVoteProposalClient.proposalVote(
                dto.getTopic(),
                dto.getDuration()
        );

        log.info("#[REST]#[To  : WEB-Client] Proposal vote response: Success={}, Message='{}', Status={}",
                grpcProposalVoteResponse.get("success"),
                grpcProposalVoteResponse.get("message"),
                grpcProposalVoteResponse.get("status")
        );
        log.info("===================================================================================================");

        return grpcProposalVoteResponse;
    }
}
