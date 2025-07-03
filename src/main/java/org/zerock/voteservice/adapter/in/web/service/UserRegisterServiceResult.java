package org.zerock.voteservice.adapter.in.web.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.zerock.voteservice.adapter.out.persistence.entity.UserEntity;
import org.zerock.voteservice.tool.hash.Sha256;

import java.time.LocalDate;


@Getter
@Builder
@AllArgsConstructor
public class UserRegisterServiceResult {
    private Boolean success;
    private String status;
    private String message;
    private Integer uid;
    private String userHash;
    private String gender;
    private LocalDate birthDate;
    private Integer httpStatusCode;

    public static UserRegisterServiceResult success(String status, UserEntity userEntity) {
        String successMessage = "신규 회원 검증 및 등록에 성공했습니다.";

        return builder()
                .success(true)
                .status(status)
                .message(successMessage)
                .uid(userEntity.getUid())
                .userHash(Sha256.sum(userEntity))
                .gender(userEntity.getGender())
                .birthDate(userEntity.getBirthDate())
                .httpStatusCode(HttpStatus.OK.value())
                .build();
    }

    public static UserRegisterServiceResult successWithoutData() {
        return builder()
                .success(true)
                .build();
    }

    public static UserRegisterServiceResult failureWithoutData() {
        return builder()
                .success(false)
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
