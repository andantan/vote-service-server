package org.zerock.voteservice.dto.query.error;

import org.springframework.http.HttpStatus;

@lombok.Getter
public enum ProposalQueryErrorStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    PROPOSAL_NOT_FOUND(
            "PROPOSAL_NOT_FOUND",
            "해당 투표를 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND // 404
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

    ProposalQueryErrorStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static ProposalQueryErrorStatus fromCode(String code) {
        for (ProposalQueryErrorStatus status : ProposalQueryErrorStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return UNKNOWN_ERROR;
    }
}
