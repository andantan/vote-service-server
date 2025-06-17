package org.zerock.voteservice.dto.query.error;

import org.springframework.http.HttpStatus;

@lombok.Getter
public enum BallotQueryErrorStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    USER_NOT_FOUND(
            "USER_NOT_FOUND",
            "해당 유권자를 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND // 404
    ),
    CACHE_ACCESS_ERROR(
            "CACHE_ACCESS_ERROR",
            "캐시 서버에서 알 수 없는 오류가 발생했습니다.",
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

    BallotQueryErrorStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static BallotQueryErrorStatus fromCode(String code) {
        for (BallotQueryErrorStatus status : BallotQueryErrorStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return UNKNOWN_ERROR;
    }
}
