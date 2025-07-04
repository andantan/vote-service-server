package org.zerock.voteservice.adapter.out.grpc.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseStatus;

@Getter
public enum GrpcBallotTransactionResponseStatus implements GrpcResponseStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    PROPOSAL_NOT_OPEN(
            "PROPOSAL_NOT_FOUND",
            "현재 존재하지 않는 투표입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    TIMEOUT_PROPOSAL(
            "TIMEOUT_PROPOSAL",
            "투표가 마감되어 정산 중입니다.",
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

    GrpcBallotTransactionResponseStatus(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatus;
    }

    public static GrpcBallotTransactionResponseStatus fromCode(String code) {
        for (GrpcBallotTransactionResponseStatus status : values()) {
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
