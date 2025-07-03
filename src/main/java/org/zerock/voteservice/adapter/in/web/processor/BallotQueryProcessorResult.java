package org.zerock.voteservice.adapter.in.web.processor;

import domain.event.ballot.query.protocol.Ballot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BallotQueryProcessorResult {
    private Boolean success;
    private String status;
    private String message;
    private List<Ballot> ballotList;
    private Integer httpStatusCode;

    public static BallotQueryProcessorResult success(String status, List<Ballot> ballotList) {
        String successMessage = "조회가 완료되었습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .ballotList(ballotList)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static BallotQueryProcessorResult successWithoutData() {
        return builder()
                .success(true)
                .build();
    }

    public static BallotQueryProcessorResult failure(String status) {
        return builder()
                .success(false)
                .status(status)
                .build();
    }
}