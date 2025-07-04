package org.zerock.voteservice.adapter.in.web.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Schema(
        description = "로그인 시도 요청 데이터 인터페이스"
)
public class UserAuthenticationRequestDto extends BaseUserRequest {
}
