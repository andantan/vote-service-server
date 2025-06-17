package org.zerock.voteservice.dto.vote.error;

import org.springframework.http.HttpStatus;

@lombok.Getter
public enum VoteProposalErrorStatus {
    PROPOSAL_EXPIRED(
            "PROPOSAL_EXPIRED",
            "이미 진행되었던 투표입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    PROPOSAL_ALREADY_OPEN(
            "PROPOSAL_ALREADY_OPEN",
            "현재 진행 중인 투표입니다.",
            HttpStatus.CONFLICT // 409
    ),
    UNKNOWN_ERROR(
            "UNKNOWN_ERROR",
            "알 수 없는 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    ),
    CACHE_ACCESS_ERROR(
            "CACHE_ACCESS_ERROR",
            "캐시 서버에서 알 수 없는 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatusCode;

    VoteProposalErrorStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static VoteProposalErrorStatus fromCode(String code) {
        for (VoteProposalErrorStatus status : VoteProposalErrorStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return UNKNOWN_ERROR;
    }
}
