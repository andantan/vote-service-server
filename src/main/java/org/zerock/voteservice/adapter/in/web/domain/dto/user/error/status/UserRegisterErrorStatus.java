package org.zerock.voteservice.adapter.in.web.domain.dto.user.error.status;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserRegisterErrorStatus {
    OK(
            "OK",
            "회원가입 성공",
            HttpStatus.OK // 200
    ),
    EXIST_USERNAME(
            "EXIST_USERNAME",
            "존재하는 로그인 아이디입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    EXIST_EMAIL(
            "EXIST_EMAIL",
            "존재하는 회원 이메일입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    EXIST_USERHASH(
            "EXIST_USERHASH",
            "존재하는 유저 해시값입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    EXIST_UID(
            "EXIST_UID",
            "존재하는 유저 UID입니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    INVALID_PARAMETER(
          "INVALID_PARAMETER",
            "유효하지 않은 회원 정보가 존재합니다.",
            HttpStatus.BAD_REQUEST // 400
    ),
    DATABASE_ACCESS_ERROR(
            "DATABASE_ACCESS_ERROR",
            "데이터베이스 서버에서 알 수 없는 오류가 발생했습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR // 500
    ),
    INTERNAL_SERVER_ERROR(
            "INTERNAL_SERVER_ERROR",
            "서버 내부의 오류가 발생했습니다.",
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

    UserRegisterErrorStatus(String code, String message, HttpStatus httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    public static UserRegisterErrorStatus fromCode(String code) {
        for (UserRegisterErrorStatus status : UserRegisterErrorStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }

        return UNKNOWN_ERROR;
    }
}
