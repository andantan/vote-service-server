package org.zerock.voteservice.adapter.in.web.domain.dto.request.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto implements RequestDto {
    private Long uid;
    private String username;
    private String userhash;
}
