package org.zerock.voteservice.adapter.in.web.domain.dto.request.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.zerock.voteservice.adapter.in.common.RequestDto;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationWebClientRequestDto implements RequestDto {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
