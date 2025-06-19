package org.zerock.voteservice.adapter.in.web.controller.query.processor;

import domain.event.proposal.query.protocol.Proposal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProposalQueryResult {
    private Boolean success;
    private String status;
    private String message;
    private Proposal proposal;
    private Integer httpStatusCode;

    public static ProposalQueryResult success(String status, Proposal proposal) {
        String successMessage = "조회가 완료되었습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .proposal(proposal)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static ProposalQueryResult successWithoutData() {
        return builder()
                .success(true)
                .build();
    }

    public static ProposalQueryResult failure(String status) {
        return builder()
                .success(false)
                .status(status)
                .build();
    }
}
