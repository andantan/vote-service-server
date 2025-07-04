package org.zerock.voteservice.adapter.out.grpc.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GrpcProposalFilteredListQueryResponseStatus implements GrpcResponseStatus  {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
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
    BUSINESS_LOGIC_ERROR(
            "BUSINESS_LOGIC_ERROR",
            "비즈니스 로직 처리 중 오류가 발생했습니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 내부에서 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatusCode;

    GrpcProposalFilteredListQueryResponseStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static GrpcProposalFilteredListQueryResponseStatus fromCode(String code) {
        for (GrpcProposalFilteredListQueryResponseStatus status : GrpcProposalFilteredListQueryResponseStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }

        return INTERNAL_SERVER_ERROR;
    }

    public boolean isOk() {
        return this == OK;
    }
}
