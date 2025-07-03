package org.zerock.voteservice.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "유저 회원 가입 응답 데이터 인터페이스"
)
public class UserRegisterResponseDto extends BaseUserResponse {
    @JsonProperty("username")
    @Schema(
            description = "유저의 로그인 ID",
            example = "userid",
            type = "string",
            implementation = String.class
    )
    private String username;
}
