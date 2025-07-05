package org.zerock.voteservice.adapter.out.grpc.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.zerock.voteservice.adapter.out.grpc.common.GrpcResponseStatus;

@Getter
public enum GrpcUserValidationResponseStatus implements GrpcResponseStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    INVALID_PARAMETER(
            "INVALID_PARAMETER",
            "유효하지 않은 데이터가 입력되었습니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    EXIST_USERHASH(
            "EXIST_USERHASH",
            "이미 존재하는 유저의 해시값입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    EXIST_UID(
            "EXIST_UID",
            "이미 존재하는 유저의 ID값입니다.",
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

    GrpcUserValidationResponseStatus(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatus;
    }

    public static GrpcUserValidationResponseStatus fromCode(String code) {
        for (GrpcUserValidationResponseStatus status : values()) {
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
