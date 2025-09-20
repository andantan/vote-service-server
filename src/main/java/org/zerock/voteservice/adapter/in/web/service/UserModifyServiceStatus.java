package org.zerock.voteservice.adapter.in.web.service;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserModifyServiceStatus {
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
    USER_NOT_EXIST(
            "USER_NOT_EXIST",
            "존재하지 않는 회원입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    DUPLICATED_PASSWORD(
            "DUPLICATED_PASSWORD",
            "현재 비밀번호와 동일합니다. 다른 비밀번호를 입력해주세요.",
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

    UserModifyServiceStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static UserModifyServiceStatus fromCode(String code) {
        for (UserModifyServiceStatus status : UserModifyServiceStatus.values()) {
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
