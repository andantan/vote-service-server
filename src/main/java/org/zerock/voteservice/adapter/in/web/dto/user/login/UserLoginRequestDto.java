package org.zerock.voteservice.adapter.in.web.dto.user.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.web.dto.user.base.BaseUserRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
//@AllArgsConstructor
@Schema(
        description = "로그인 시도 요청 데이터 인터페이스"
)
public class UserLoginRequestDto extends BaseUserRequest {
}