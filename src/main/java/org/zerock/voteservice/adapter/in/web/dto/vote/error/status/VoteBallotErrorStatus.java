package org.zerock.voteservice.adapter.in.web.dto.vote.error.status;

import org.springframework.http.HttpStatus;

@lombok.Getter
public enum VoteBallotErrorStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    DUPLICATE_VOTE_SUBMISSION(
            "DUPLICATE_VOTE_SUBMISSION",
            "이미 참가한 투표입니다. (재투표 불가)",
            HttpStatus.CONFLICT // 409
    ),
    PROPOSAL_NOT_OPEN(
            "PROPOSAL_NOT_FOUND",
            "현재 존재하지 않는 투표입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    TIMEOUT_PROPOSAL(
            "TIMEOUT_PROPOSAL",
            "투표가 마감되어 정산 중입니다.",
            HttpStatus.NOT_ACCEPTABLE // 406
    ),
    INVALID_OPTION(
            "INVALID_OPTION",
            "비정상적인 투표 선택 사항입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_HASH_LENGTH(
            "INVALID_HASH_LENGTH",
            "비정상적인 해시값입니다. (해시 길이 오류)",
            HttpStatus.BAD_REQUEST // 400
    ),
    DECODE_ERROR(
            "DECODE_ERROR",
            "비정상적인 해시값입니다. (해시 해독 오류)",
            HttpStatus.BAD_REQUEST // 400
    ),
    PROPOSAL_EXPIRED(
            "PROPOSAL_EXPIRED",
            "이미 진행이 완료된 투표입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    DATABASE_ACCESS_ERROR(
            "DATABASE_ACCESS_ERROR",
            "데이터베이스 서버에서 알 수 없는 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    ),
    UNKNOWN_ERROR(
            "UNKNOWN_ERROR",
            "알 수 없는 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatusCode;

    VoteBallotErrorStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static VoteBallotErrorStatus fromCode(String code) {
        for (VoteBallotErrorStatus status : VoteBallotErrorStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return UNKNOWN_ERROR;
    }
}
