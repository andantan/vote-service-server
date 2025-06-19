package org.zerock.voteservice.adapter.in.web.controller.vote.processor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class BallotCreateResult {
    private Boolean success;
    private String status;
    private String message;
    private String voteHash;
    private Integer httpStatusCode;

    public static BallotCreateResult success(String status, String voteHash) {
        String successMessage = "투표 참여가 완료되었습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .voteHash(voteHash)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static BallotCreateResult failure(String status) {
        return builder()
                .success(false)
                .status(status)
                .build();
    }
}
