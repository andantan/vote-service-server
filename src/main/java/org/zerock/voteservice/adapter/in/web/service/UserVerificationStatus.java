package org.zerock.voteservice.adapter.in.web.service;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserVerificationStatus {
    OK(
            "OK",
            "요청 처리 성공",
            HttpStatus.OK // 200
    ),
    UNKNOWN_USER(
      "UNKNOWN_USER",
      "회원 정보를 찾을 수 없습니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    MALFORMED_CODE(
            "MALFORMED_CODE",
            "인증 코드 유효 기간을 초과하였습니다. 인증을 다시 진행해주세요.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_CODE(
            "INVALID_CODE",
            "인증 코드가 일치하지 않습니다. 인증을 진행해주세요.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_PARAMETER(
            "INVALID_PARAMETER",
            "회원 정보와 일치하지 않거나, 잘못된 회원 가입 정보입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 내부의 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatusCode;

    UserVerificationStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static UserVerificationStatus fromCode(String code) {
        for (UserVerificationStatus status : UserVerificationStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }

        return INTERNAL_SERVER_ERROR;
    }

    public boolean isOk() {
        return this == OK;
    }
}
