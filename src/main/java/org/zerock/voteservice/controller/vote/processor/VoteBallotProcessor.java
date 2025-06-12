package org.zerock.voteservice.controller.vote.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import org.zerock.voteservice.dto.vote.VoteBallotDto;
import org.zerock.voteservice.grpc.event.GrpcBallotEventClient;
import org.zerock.voteservice.grpc.vote.GrpcBallotTransactionClient;
import org.zerock.voteservice.property.event.GrpcBallotEventConnectionProperties;
import org.zerock.voteservice.property.vote.GrpcBallotTransactionConnectionProperties;

import domain.event.ballot.protocol.ValidateBallotEventResponse;
import domain.event.ballot.protocol.CacheBallotEventResponse;
import domain.vote.submit.protocol.SubmitBallotTransactionResponse;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class VoteBallotProcessor {
    private final GrpcBallotTransactionClient grpcBallotTransactionClient;
    private final GrpcBallotEventClient grpcBallotEventClient;

    public VoteBallotProcessor(
            GrpcBallotTransactionConnectionProperties grpcBallotTransactionConnectionProperties,
            GrpcBallotEventConnectionProperties grpcBallotEventConnectionProperties
    ) {
        this.grpcBallotTransactionClient = new GrpcBallotTransactionClient(
                grpcBallotTransactionConnectionProperties.getHost(), grpcBallotTransactionConnectionProperties.getPort()
        );
        this.grpcBallotEventClient = new GrpcBallotEventClient(
                grpcBallotEventConnectionProperties.getHost(), grpcBallotEventConnectionProperties.getPort()
        );
    }

    public ValidateBallotEventResponse validateBallot(VoteBallotDto dto) {
        return this.grpcBallotEventClient.validateBallot(dto.getUserHash(), dto.getTopic());
    }

    public SubmitBallotTransactionResponse submitBallotTransaction(VoteBallotDto dto) {
        return this.grpcBallotTransactionClient.submitBallotTransaction(
                dto.getUserHash(), dto.getTopic(), dto.getOption()
        );
    }

    public CacheBallotEventResponse cacheBallot(VoteBallotDto dto, String voteHash) {
        return this.grpcBallotEventClient.cacheBallot(
                dto.getUserHash(), voteHash, dto.getTopic(), dto.getOption()
        );
    }

    public Map<String, String> getSuccessResponse(VoteBallotDto dto, String voteHash) {
        Map<String, String> response = new HashMap<>();

        response.put("success", "true");
        response.put("status", "OK");
        response.put("message", "투표 참여가 완료되었습니다.");
        response.put("user_hash", dto.getUserHash());
        response.put("vote_hash", voteHash);
        response.put("topic", dto.getTopic());
        response.put("option", dto.getOption());

        return response;
    }

    public Map<String, String> getErrorResponse(VoteBallotDto dto, String status) {
        Map<String, String> response = new HashMap<>();

        response.put("success", "false");
        response.put("status", status);

        switch (status) {
            case "DUPLICATE_VOTE_SUBMISSION" -> response.put("message", "이미 참가한 투표입니다. (재투표 불가)");
            case "PROPOSAL_NOT_OPEN" -> response.put("message", "현재 존재하지 않는 투표입니다.");
            case "TIMEOUT_PROPOSAL" -> response.put("message", "투표가 마감되어 정산 중입니다.");
            case "INVALID_HASH_LENGTH" -> response.put("message", "비정상적인 해시값입니다. (해시 길이 오류)");
            case "DECODE_ERROR" -> response.put("message", "배정상적인 해시값입니다. (해시 해독 오류)");
            case "UNKNOWN_ERROR" -> response.put("message", "알수 없는 오류");
            default -> response.put("message", "");
        }

        response.put("user_hash", dto.getUserHash());
        response.put("topic", dto.getTopic());
        response.put("option", dto.getOption());

        return response;
    }
}
