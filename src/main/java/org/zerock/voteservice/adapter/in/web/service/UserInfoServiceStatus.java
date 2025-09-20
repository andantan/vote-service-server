package org.zerock.voteservice.adapter.in.web.service;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserInfoServiceStatus {
    OK(
        "OK",
        "요청 처리 성공",
        HttpStatus.OK // 200
    ),
    ABNORMAL_REQUEST(
        "ABNORMAL_REQUEST",
        "조회 대상과 맞지 않는 정보입니다.",
        HttpStatus.BAD_REQUEST // 400
    ),
    NOT_EXIST_UID(
        "NOT_EXIST_UID",
        "존재하지 않는 회원 UID입니다.",
        HttpStatus.BAD_REQUEST // 400
    ),
    NOT_EXIST_USERNAME(
        "NOT_EXIST_USERNAME",
        "존재하지 않는 로그인 아이디입니다.",
        HttpStatus.BAD_REQUEST // 400
    ),
    NOT_EXIST_EMAIL(
        "NOT_EXIST_EMAIL",
        "존재하는 않는 회원 이메일입니다.",
        HttpStatus.BAD_REQUEST // 400
    ),
    NOT_EXIST_PHONENUMBER(
        "NOT_EXIST_PHONENUMBER",
        "존재하지 않는 회원 전화번호입니다.",
        HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_PARAMETER(
        "INVALID_PARAMETER",
        "유효하지 않은 데이터가 입력되었습니다.",
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

    UserInfoServiceStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static UserInfoServiceStatus fromCode(String code) {
        for (UserInfoServiceStatus status : UserInfoServiceStatus.values()) {
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
