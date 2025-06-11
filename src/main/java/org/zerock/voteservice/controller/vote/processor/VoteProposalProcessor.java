package org.zerock.voteservice.controller.vote.processor;

import domain.vote.proposal.protocol.OpenProposalResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import domain.event.proposal.protocol.ValidateProposalEventResponse;
import domain.event.proposal.protocol.CacheProposalEventResponse;

import org.zerock.voteservice.dto.vote.VoteProposalDto;
import org.zerock.voteservice.grpc.event.GrpcProposalEventClient;
import org.zerock.voteservice.grpc.vote.GrpcProposalPendingClient;
import org.zerock.voteservice.property.event.GrpcProposalEventConnectionProperties;
import org.zerock.voteservice.property.vote.GrpcProposalPendingConnectionProperties;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class VoteProposalProcessor {
    private final GrpcProposalPendingClient grpcProposalPendingClient;
    private final GrpcProposalEventClient grpcProposalEventClient;

    public VoteProposalProcessor(
            GrpcProposalPendingConnectionProperties grpcProposalPendingConnectionProperties,
            GrpcProposalEventConnectionProperties grpcProposalEventConnectionProperties
    ) {
        this.grpcProposalPendingClient = new GrpcProposalPendingClient(
                grpcProposalPendingConnectionProperties.getHost(), grpcProposalPendingConnectionProperties.getPort()
        );
        this.grpcProposalEventClient = new GrpcProposalEventClient(
                grpcProposalEventConnectionProperties.getHost(), grpcProposalEventConnectionProperties.getPort()
        );
    }

    public ValidateProposalEventResponse validateProposal(VoteProposalDto dto) {
        return this.grpcProposalEventClient.validateProposal(dto.getTopic());
    }

    public OpenProposalResponse requestOpenPending(VoteProposalDto dto) {
         return this.grpcProposalPendingClient.openProposalPending(dto.getTopic(), dto.getDuration());
    }

    public CacheProposalEventResponse requestCacheProposal(VoteProposalDto dto) {
        return this.grpcProposalEventClient.cacheProposal(dto.getTopic(), dto.getDuration());
    }

    public Map<String, String> getSuccessResponse(VoteProposalDto dto) {
        Map<String, String> response = new HashMap<>();

        response.put("success", "true");
        response.put("status", "OK");
        response.put("message", "투표 등록이 완료되었습니다.");
        response.put("topic", dto.getTopic());
        response.put("duration", String.valueOf(dto.getDuration()));

        return response;
    }

    public Map<String, String> getErrorResponse(VoteProposalDto dto, String status) {
        Map<String, String> response = new HashMap<>();

        response.put("success", "false");
        response.put("status", status);

        switch (status) {
            case "PROPOSAL_EXPIRED" -> response.put("message", "이미 진행되었던 투표입니다.");
            case "PROPOSAL_ALREADY_OPEN" -> response.put("message", "현재 진행 중인 투표입니다.");
            case "UNKNOWN_ERROR" -> response.put("message", "알수 없는 오류");
            default -> response.put("message", "");
        }

        response.put("topic", dto.getTopic());
        response.put("duration", String.valueOf(dto.getDuration()));

        return response;
    }
}
