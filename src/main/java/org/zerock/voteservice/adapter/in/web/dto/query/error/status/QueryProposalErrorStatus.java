package org.zerock.voteservice.adapter.in.web.dto.query.error.status;

import org.springframework.http.HttpStatus;

@lombok.Getter
public enum QueryProposalErrorStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    INVALID_PARAMETER(
      "INVALID_PARAMETER",
      "유효하지 않은 투표 제목입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_SORT_ORDER_PARAM(
            "INVALID_SORT_ORDER_PARAM",
            "sortOrder 값은 'asc' 또는 'desc'만 가능합니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_SORT_BY_PARAM(
            "INVALID_SORT_BY_PARAM",
            "sortOrder와 sortBy 파라미터는 함께 사용하거나 모두 생략해야 합니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    PAGING_OUT_OF_BOUNDS(
            "PAGING_OUT_OF_BOUNDS",
            "조회 가능한 페이지 범위를 벗어났습니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    SKIP_ZERO_PARAM(
            "SKIP_ZERO_PARAM",
            "요청된 Skip 값이 유효하지 않습니다. Skip은 0보다 커야 합니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    LIMIT_ZERO_PARAM(
            "LIMIT_ZERO_PARAM",
            "요청된 Limit 값이 유효하지 않습니다. Limit은 0보다 커야 합니다.",
            HttpStatus.BAD_REQUEST // 400
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

    QueryProposalErrorStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static QueryProposalErrorStatus fromCode(String code) {
        for (QueryProposalErrorStatus status : QueryProposalErrorStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return UNKNOWN_ERROR;
    }
}
