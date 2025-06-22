package org.zerock.voteservice.adapter.in.web.controller.vote.proposal.processor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ProposalCreateResult {
    private Boolean success;
    private String status;
    private String message;
    private String topic;
    private Integer httpStatusCode;

    public static ProposalCreateResult success(String status, String topic) {
        String successMessage = "투표 등록이 완료되었습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .topic(topic)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static ProposalCreateResult failure(String status) {
        return builder()
                .success(false)
                .status(status)
                .build();
    }
}
