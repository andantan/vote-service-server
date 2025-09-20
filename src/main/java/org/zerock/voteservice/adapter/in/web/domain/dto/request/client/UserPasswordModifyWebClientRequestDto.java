package org.zerock.voteservice.adapter.in.web.domain.dto.request.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zerock.voteservice.adapter.in.common.extend.RestApiRequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordModifyWebClientRequestDto implements RestApiRequestDto {
    @JsonProperty("uid")
    private Long uid;

    @JsonProperty("username")
    private String username;

    @JsonProperty("new_password")
    private String newPassword;
}
