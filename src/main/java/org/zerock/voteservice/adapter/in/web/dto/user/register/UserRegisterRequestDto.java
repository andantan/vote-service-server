package org.zerock.voteservice.adapter.in.web.dto.user.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.user.base.BaseUserRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "회원 가입 요청 데이터 인터페이스"
)
public class UserRegisterRequestDto extends BaseUserRequest {
    @JsonProperty("real_name")
    @Schema(
            description = "사용자 이름",
            example = "홍길동",
            type = "string",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String realName;

    @JsonProperty("birth_date")
    @Schema(
            description = "사용자 생년월일 (YYMMDD-G 형식)",
            example = "001209-3",
            type = "string",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String birthDate;

    @JsonProperty("email")
    @Schema(
            description = "사용자 이메일 주소",
            example = "user@example.com",
            type = "string",
            format = "email",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @JsonProperty("phone_number")
    @Schema(
            description = "사용자 전화번호",
            example = "01012345678",
            type = "string",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String phoneNumber;
}
