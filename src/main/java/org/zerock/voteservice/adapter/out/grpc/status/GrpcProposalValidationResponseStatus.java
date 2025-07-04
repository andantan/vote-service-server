package org.zerock.voteservice.adapter.out.grpc.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GrpcProposalValidationResponseStatus implements GrpcResponseStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
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

    GrpcProposalValidationResponseStatus(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatus;
    }

    public static GrpcProposalValidationResponseStatus fromCode(String code) {
        for (GrpcProposalValidationResponseStatus status : values()) {
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
