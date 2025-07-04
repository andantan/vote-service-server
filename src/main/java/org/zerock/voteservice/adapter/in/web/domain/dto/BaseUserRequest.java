package org.zerock.voteservice.adapter.in.web.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseUserRequest {
    @JsonProperty("username")
    @Schema(
            description = "사용자 로그인 아이디",
            example = "user123",
            type = "string",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @JsonProperty("password")
    @Schema(
            description = "사용자 비밀번호",
            example = "password!@#123",
            type = "string",
            implementation = String.class,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}
