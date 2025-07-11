package org.zerock.voteservice.adapter.in.web.domain.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.zerock.voteservice.adapter.in.common.extend.CommonResponseDto;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
public class UserRefreshTokenSuccessResponseDto extends CommonResponseDto {
}
