package org.zerock.voteservice.adapter.in.web.domain.dto;

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
        description = "로그인 시도 응답 데이터 인터페이스"
)
public class UserAuthenticationResponseDto extends BaseUserResponse {
    @JsonProperty("expire_minutes")
    @Schema(
            description = "AccessToken 만료 기간 (분 단위)",
            example = "3600",
            type = "integer",
            format = "int64",
            implementation = Long.class
    )
    private Long expireMinutes;
}
