package org.zerock.voteservice.adapter.in.web.controller.query.proposal.processor;

import domain.event.proposal.query.protocol.Proposal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProposalQueryProcessorResult {
    private Boolean success;
    private String status;
    private String message;
    private Proposal proposal;
    private List<Proposal> proposalList;
    private Integer httpStatusCode;

    public static ProposalQueryProcessorResult success(String status, Proposal proposal) {
        String successMessage = "조회가 완료되었습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .proposal(proposal)
                .proposalList(null)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static ProposalQueryProcessorResult successForDetailList(String status, List<Proposal> proposalList) {
        String successMessage = "조회가 완료되었습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .proposal(null)
                .proposalList(proposalList)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }


    public static ProposalQueryProcessorResult successWithoutData() {
        return builder()
                .success(true)
                .build();
    }

    public static ProposalQueryProcessorResult failure(String status) {
        return builder()
                .success(false)
                .status(status)
                .build();
    }
}
