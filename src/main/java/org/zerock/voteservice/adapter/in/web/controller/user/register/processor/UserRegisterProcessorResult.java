package org.zerock.voteservice.adapter.in.web.controller.user.register.processor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class UserRegisterProcessorResult {
    private Boolean success;
    private String status;
    private String message;
    private Integer uid;
    private String userHash;
    private Integer httpStatusCode;

    public static UserRegisterProcessorResult success(String status, Integer uid, String userHash) {
        String successMessage = "신규 회원 검증 및 등록에 성공했습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .uid(uid)
                .userHash(userHash)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static UserRegisterProcessorResult failure(String status) {
        return builder()
                .success(false)
                .status(status)
                .build();
    }
}
