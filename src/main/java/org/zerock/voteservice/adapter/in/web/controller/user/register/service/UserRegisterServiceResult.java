package org.zerock.voteservice.adapter.in.web.controller.user.register.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
public class UserRegisterServiceResult {
    private Boolean success;
    private String status;
    private String message;
    private Integer id;
    private LocalDateTime registerTime;
    private Integer httpStatusCode;

    public static UserRegisterServiceResult success(String status, Integer id) {
        String successMessage = "신규 회원 검증 및 등록에 성공했습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .id(id)
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static UserRegisterServiceResult successWithoutData() {
        return builder()
                .success(true)
                .build();
    }

    public static UserRegisterServiceResult failure(String status) {
        return builder()
                .success(false)
                .status(status)
                .build();
    }

    public static UserRegisterServiceResult failureWithMessage(String status, String message) {
        return builder()
                .success(false)
                .status(status)
                .message(message)
                .build();
    }

    public boolean isExistMessage() {
        return this.message != null;
    }
}
