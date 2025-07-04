package org.zerock.voteservice.adapter.in.web.domain.dto.query.error.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum QueryBallotErrorStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    DECODE_ERROR(
            "DECODE_ERROR",
            "유효하지 않은 해시입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_HASH_LENGTH(
            "INVALID_HASH_LENGTH",
            "유효하지 않은 해시 길이입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    USER_NOT_FOUND(
            "USER_NOT_FOUND",
            "해당 유권자를 찾을 수 없습니다.",
            HttpStatus.NOT_FOUND // 404
    ),
    DATABASE_ACCESS_ERROR(
            "DATABASE_ACCESS_ERROR",
            "데이터베이스 서버에서 알 수 없는 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 내부에서 오류가 발생했습니다.",
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

    QueryBallotErrorStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static QueryBallotErrorStatus fromCode(String code) {
        for (QueryBallotErrorStatus status : QueryBallotErrorStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return UNKNOWN_ERROR;
    }
}
