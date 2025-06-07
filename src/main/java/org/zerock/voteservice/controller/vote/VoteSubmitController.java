package org.zerock.voteservice.controller.vote;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.voteservice.grpc.event.GrpcBallotEventClient;
import org.zerock.voteservice.property.event.GrpcBallotEventConnectionProperties;
import org.zerock.voteservice.property.vote.GrpcVoteSubmitConnectionProperties;
import org.zerock.voteservice.grpc.vote.GrpcVoteSubmitClient;
import org.zerock.voteservice.dto.vote.VoteSubmitDto;

import java.util.Map;

@Log4j2
@RestController
public class VoteSubmitController extends VoteRequestMapper {
    private final GrpcVoteSubmitClient grpcVoteClient;
    private final GrpcBallotEventClient grpcBallotEventClient;

    public VoteSubmitController(
            GrpcVoteSubmitConnectionProperties grpcVoteConnectionProps,
            GrpcBallotEventConnectionProperties grpcBallotEventConnectionProperties
    ) {
        this.grpcVoteClient = new GrpcVoteSubmitClient(
                grpcVoteConnectionProps.getHost(), grpcVoteConnectionProps.getPort()
        );
        this.grpcBallotEventClient = new GrpcBallotEventClient(
                grpcBallotEventConnectionProperties.getHost(), grpcBallotEventConnectionProperties.getPort()
        );
    }

    @PostMapping("/submit")
    public Map<String,String> submitVote(@RequestBody VoteSubmitDto dto) {
        log.info("===================================================================================================");
        log.info("#[REST]#[From: WEB-Client] Submit-vote request received: topic='{}', hash={}, option={}",
                dto.getTopic(),
                dto.getHash(),
                dto.getOption()
        );

        Map<String, Object> grpcBallotValidationResponse = grpcBallotEventClient.validateNewBallotEvent(
                dto.getTopic(),
                dto.getHash(),
                dto.getOption()
        );

        Map<String, String> grpcSubmitVoteResponse = grpcVoteClient.submitVote(
                dto.getHash(),
                dto.getOption(),
                dto.getTopic()
        );

        log.info("#[REST]#[To  : WEB-Client] Proposal vote response: Success={}, Message='{}', Status={}",
                grpcSubmitVoteResponse.get("success"),
                grpcSubmitVoteResponse.get("message"),
                grpcSubmitVoteResponse.get("status")
        );
        log.info("===================================================================================================");

        return grpcSubmitVoteResponse;
    }
}
