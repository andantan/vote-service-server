package org.zerock.voteservice.adapter.out.grpc.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseStatus;

@Getter
public enum GrpcBallotValidationResponseStatus implements GrpcResponseStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    DUPLICATE_VOTE_SUBMISSION(
            "DUPLICATE_VOTE_SUBMISSION",
            "이미 참가한 투표입니다. (재투표 불가)",
            HttpStatus.BAD_REQUEST // 400
    ),
    PROPOSAL_NOT_OPEN(
            "PROPOSAL_NOT_FOUND",
            "현재 존재하지 않는 투표입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_OPTION(
            "INVALID_OPTION",
            "비정상적인 투표 선택 사항입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    PROPOSAL_EXPIRED(
            "PROPOSAL_EXPIRED",
            "이미 진행이 완료된 투표입니다.",
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

    GrpcBallotValidationResponseStatus(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatus;
    }

    public static GrpcBallotValidationResponseStatus fromCode(String code) {
        for (GrpcBallotValidationResponseStatus status : values()) {
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
