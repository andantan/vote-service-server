package org.zerock.voteservice.adapter.out.grpc.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GrpcRuntimeStatus {
    OK(
            "OK",
            "서버와의 통신에 성공했습니다.",
            HttpStatus.OK // 200
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 내부에서 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    ),
    BAD_GATEWAY(
            "BAD_GATEWAY",
            "서버와의 통신 중 문제가 발생했습니다.",
            HttpStatus.BAD_GATEWAY // 502
    ),
    SERVICE_UNAVAILABLE(
            "SERVICE_UNAVAILABLE",
            "서버와의 통신이 불가능합니다.",
            HttpStatus.SERVICE_UNAVAILABLE // 503
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatusCode;

    GrpcRuntimeStatus(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatus;
    }

    public static GrpcRuntimeStatus fromCode(String code) {
        for (GrpcRuntimeStatus status : GrpcRuntimeStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }

        return BAD_GATEWAY;
    }

    public boolean isOk() {
        return this == OK;
    }
}
