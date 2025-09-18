package org.zerock.voteservice.adapter.in.web.domain.dto.request.client;

import lombok.Builder;
import lombok.Getter;
import org.zerock.voteservice.adapter.in.common.extend.RestApiRequestDto;

@Getter
@Builder
public class UserInfoWebClientRequestDto implements RestApiRequestDto {
    private Long uid;
    private String username;
    private String userhash;
}
