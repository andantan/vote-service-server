package org.zerock.voteservice.adapter.in.web.domain.dto.user.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.zerock.voteservice.adapter.in.web.domain.dto.ResponseDto;
import org.zerock.voteservice.adapter.in.web.domain.dto.user.error.status.UserRegisterErrorStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@Schema(
        description = "User API 오류 응답 공통 형식"
)
public class UserErrorResponseDto implements ResponseDto {
    @JsonProperty("success")
    @Schema(
            description = "요청 처리 성공 여부",
            example = "true",
            type = "boolean",
            implementation = Boolean.class
    )
    private Boolean success;

    @JsonProperty("message")
    @Schema(
            description = "오류 상세 메시지",
            example = "존재하는 회원 아이디 입니다.",
            type = "string",
            implementation = String.class
    )
    private String message;

    @JsonProperty("status")
    @Schema(
            description = "내부 오류 코드",
            example = "EXIST_USERNAME",
            type = "string",
            implementation = String.class
    )
    private String status;

    @JsonProperty("http_status_code")
    @Schema(
            description = "HTTP 응답 상태 코드",
            example = "400",
            type = "number",
            implementation = Integer.class
    )
    private Integer httpStatusCode;

    public static UserErrorResponseDto from(UserRegisterErrorStatus errorStatus) {
        return UserErrorResponseDto.builder()
                .success(false)
                .message(errorStatus.getMessage())
                .status(errorStatus.getCode())
                .httpStatusCode(errorStatus.getHttpStatusCode().value())
                .build();
    }

    public static UserErrorResponseDto fromWithCustomMessage(UserRegisterErrorStatus errorStatus, String errorMessage) {
        return UserErrorResponseDto.builder()
                .success(false)
                .message(errorMessage)
                .status(errorStatus.getCode())
                .httpStatusCode(errorStatus.getHttpStatusCode().value())
                .build();
    }
}
